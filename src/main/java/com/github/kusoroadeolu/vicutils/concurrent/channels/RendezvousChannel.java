package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//Some changes made to this channel in comparison to my unbuffered channel which had less throughput. Also some semantic changes
/** Invariants
 * 1. Channel state can only move in this direction. Nil -> Open -> Closed
 * 2. Sending on a NIL or CLOSED channels throws(Nil ex or Closed ex). It no longer blocks indefinitely
 * 3. Receiving on a NIL channel throws
 * 4. Receiving on a closed channel returns an empty {@linkplain Optional}, unless T is not null
 * 5. Producers block until their value has been consumed by a consumer
 * 6. Producers also wait but don't replace T if another producer is blocked
 * 7. Consumers block if T is null, until it isn't
 * */
public class RendezvousChannel<T> implements Channel<T>{
    private volatile T t;
    private final Lock lock;
    private final Condition isFull;
    private final Condition isEmpty;
    private final Condition itemConsumed;
    private final AtomicReference<ChannelState> state;

    public RendezvousChannel() {
        this.lock = new ReentrantLock();
        this.isEmpty = this.lock.newCondition(); //Condition for waiting takes
        this.isFull = this.lock.newCondition(); //Condition for waiting puts
        this.itemConsumed = this.lock.newCondition(); //Condition for threads that have put but are waiting for their value to be taken
        this.state = new AtomicReference<>(ChannelState.NIL);
    }

    //Ok, one bug I just ran into. It was an issue with my signalling order.
    // The item consumed condition / thread had to set T to null outside it's loop,
    // but the thread waits indefinitely in its loop, since well T has to be null before it can leave lol
    // I fixed this by making the consumer set T to null
    //The thread that waits on a condition must be the one that invalidates it.
    /*
    * Like this scenario:
        Producer A sets t = "hello" and waits
        Producer B tries to send, sees t != null (it's "hello"), waits on isFull
        Consumer takes "hello", sets t = null, signals itemConsumed
        Producer A wakes up but Producer B also wakes up (somehow gets through)
        Producer B sets t = "world"
        Now both A and B are checking while (t != null) and BOTH see t = "world"
        Both wait on itemConsumed → deadlock because only one signal will come
    * */

    @Override
    public void send(T val) {
        Objects.requireNonNull(val);
        this.validateOnSend();
        this.lock.lock();
            try {
                this.validateOnSend();
                while (t != null){
                    if (this.isClosed()) {
                        throw new ChannelClosedException("Channel is closed");
                    }
                    isFull.awaitUninterruptibly();
                }

                t = val; //Set t to val

                isEmpty.signal();

                //So here, the issue causing the blockage was me checking if t was != null, this caused the issue where there we're more than one threads waiting on the items consumed condition
                //The fix was rather than check if t was not null, we were checking if t is equals to the reference we set, therefore, no two threads can wait on item consumed
                //Still I'm confused how a race condition could occur if only one thread could hold this lock? Is it
                // Hmm might debug that later
                while (t == val && !this.isClosed()) { //Using == here to actually ensure we're comparing the reference of val to T, not the value, cuz that could cause issues
                    this.itemConsumed.awaitUninterruptibly();
                }

                this.isFull.signal();
            }finally {
                this.lock.unlock();
            }

    }

    @Override
    public Optional<T> receive() {
        T val;
        if (this.isNil()) throw new ChannelNilException("Channel is Nil");
        else if (this.isClosed() && this.isEmpty()) return Optional.empty();
        else {
            this.lock.lock();
            if (this.isNil()) throw new ChannelNilException("Channel is Nil");
            else if (this.isClosed() && this.isEmpty()) return Optional.empty();
            try {
                while ((val = t) == null && !this.isClosed()){
                    this.isEmpty.awaitUninterruptibly();
                }

                t = null; //Set t to null

                itemConsumed.signal();
            }finally {
                this.lock.unlock();
            }
        }

        return Optional.ofNullable(val);
    }

    @Override
    public SendChannel<T> makeSendChannel() {
        this.make();
        return this;
    }

    @Override
    public ReceiveChannel<T> makeReceiveChannel() {
        this.make();
        return this;
    }


    //Receive semantics but doesn't block
    @Override
    public Optional<T> tryReceive() {
        T val;
        if (this.isNil()) throw new ChannelNilException("Channel is Nil");
        else if (this.isClosed() && this.isEmpty()) return Optional.empty();

        lock.lock();
        try {
            if (this.isNil()) throw new ChannelNilException("Channel is Nil");
            else if (this.isClosed() && this.isEmpty()) return Optional.empty();
            val = t;
            if (val != null) {
                t = null;
                this.itemConsumed.signal();
            }
        }finally {
            this.lock.unlock();
        }

        return Optional.ofNullable(val);
    }



    @Override
    public boolean trySend(T val) {
        boolean res = false;
        Objects.requireNonNull(val);
        this.validateOnSend();

        lock.lock();
        try {
            this.validateOnSend();
            if (t == null){
                t = val;
                res = true;
            }
        }finally {
            this.lock.unlock();
        }

        return res;
    }


    void validateOnSend(){
        if (this.isNil()) throw new ChannelNilException("Channel is Nil");
        else if (this.isClosed()) throw new ChannelClosedException("Channel is Closed");
    }


    @Override
    public void close() {
        if (!this.state.compareAndSet(ChannelState.OPEN, ChannelState.CLOSED)){
            if (this.isClosed()){
                throw new ChannelClosedException("Channel is already closed");
            }
            else if (this.isNil()) throw new ChannelNilException("Channel is Nil");
        }

        this.lock.lock();
        try {
            this.isFull.signalAll();
            this.isEmpty.signalAll();
            this.itemConsumed.signal();
        }finally {
            this.lock.unlock();;
        }
        //Then signal all waiters
    }

    @Override
    public int capacity() {
        if (this.isNil()) return 0;
        else return 1;
    }

    @Override
    public int length() {
        return isEmpty() ? 0 : 1;
    }

    @Override
    public boolean ok() {
        return !this.isClosed() && !this.isNil();
    }

    @Override
    public boolean isEmpty() {
        return t == null;
    }


    @Override
    public void make() {
        if (!state.compareAndSet(ChannelState.NIL, ChannelState.OPEN)){
            if (this.isClosed()) throw new ChannelClosedException("Channel is already closed");
        }
    }

    boolean isClosed(){
        return state.get() == ChannelState.CLOSED;
    }

    boolean isNil(){
        return state.get() == ChannelState.NIL;
    }

    boolean isOpen(){
        return state.get() == ChannelState.OPEN;
    }

    private enum ChannelState{
        NIL, OPEN, CLOSED;
    }
}
