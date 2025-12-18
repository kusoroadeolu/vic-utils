package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.*;

import static java.util.Objects.requireNonNull;

public class UnBufferedChannel<T> implements Channel<T> {
     final BlockingQueue<T> queue;
     final Lock modifyingLock; //Mutex lock for queue even though its thread safe.
    // It's mostly to allow threads to wait and decouple the channel's state from queue and thread semantics.
    // Might be questionable, but I believe it's clearer in the long run and prevents deadlocks and multi thread contention on a single lock
    // plus prevents sleeping threads from trying to reacquire the modifying lock
     final Lock stateLock;
     final Condition isFullCondition;
     final Condition isEmptyCondition;
     int capacity;
     volatile State channelState;
     private final static int MAX_CAPACITY = 1;
     private final static String CHANNEL_CLOSED_MESSAGE = "Channel is already closed";
     private final static String CHANNEL_NIL_MESSAGE = "Channel is nil";

    public UnBufferedChannel(){
        this.capacity = MAX_CAPACITY;
        this.queue = new ArrayBlockingQueue<>(this.capacity);
        this.modifyingLock = new ReentrantLock();
        this.stateLock = new ReentrantLock();
        this.isFullCondition = this.modifyingLock.newCondition();
        this.isEmptyCondition = this.modifyingLock.newCondition();
        this.channelState = State.NIL;
    }

    public void make(){
        verifyIfClosed();
        this.stateLock.lock();
        try {
            verifyIfClosed();
            if(this.isNil()) this.channelState = State.OPEN;
        }finally {
            this.stateLock.unlock();
        }
    }

    public SendChannel<T> makeSendChannel(){
        this.make();
        return this;
    }

    public ReceiveChannel<T> makeReceiveChannel(){
        this.make();
        return this;
    }

    public void send(T val){
        requireNonNull(val);
        this.verifyIfClosed();
        this.modifyingLock.lock();
        try {
            while (!this.isEmpty() || this.isNil()) {
                this.isFullCondition.await();  //Block indefinitely if the queue is not empty initially or the channel is nil
            }

            this.queue.add(val);
            this.isEmptyCondition.signal();

            while (!this.isEmpty()){
                this.isFullCondition.await(); //Block again till the queue is empty
            }

        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } finally {
            this.modifyingLock.unlock();
        }
    }

    public Optional<T> receive() {
        if (this.isClosed() && isEmpty()) return Optional.empty();
        this.modifyingLock.lock();
        T val = null;
        try {
            while (((val = this.queue.poll()) == null && !isClosed()) || this.isNil()){
                //Block indefinitely if the channel does not have value and is not closed or the channel is NIL.
                // Awaken only if the channel has closed or a new value arrived
                this.isEmptyCondition.await();
            }

            this.isFullCondition.signal();
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } finally {
            this.modifyingLock.unlock();
        }

        return val == null ? Optional.empty() : Optional.of(val);
    }


    public boolean trySend(T val) {
        requireNonNull(val);
        this.verifyIfClosed();
        try {
           return this.queue.add(val);
        }catch (RejectedExecutionException e){
            return false;
        }
    }

    public T tryReceive() {
        return this.queue.poll();
    }

    //The total cap of the queue
    public int capacity() {
        if (this.isNil()) return 0; //If the channel is nil return 0
        else return this.capacity;
    }

    //Number of T in the queue
    public int length() {
        return this.queue.size();
    }

    public boolean isEmpty(){
        return this.queue.isEmpty();
    }


    public boolean ok() {
        return !this.isClosed();
    }

    public Iterator<T> iterator() {
        return new ChannelIterator<>(this);
    }

    public void close(){
        this.stateLock.lock();
        try {
            if (this.isNil()) throw new ChannelNilException(CHANNEL_NIL_MESSAGE);
            this.verifyIfClosed();
            this.channelState = State.CLOSED;
            this.isEmptyCondition.signalAll();
        }finally {
            this.stateLock.unlock();
        }
    }

    //Helper method for Channel#receive to allow users to drain the channel after close

    void verifyIfClosed(){
        if (this.isClosed()) throw new ChannelClosedException(CHANNEL_CLOSED_MESSAGE);
    }

    boolean isClosed(){
        return this.channelState.isClosed();
     }

    boolean isNil(){
        return this.channelState.isNil();
    }

    boolean isFull(){
        return this.length() >= this.capacity;
    }


     enum State{
        NIL,
        OPEN,
        CLOSED;

        boolean isNil(){
            return this == NIL;
        }

        boolean isOpen(){
            return this == OPEN;
        }

        boolean isClosed(){
            return this == CLOSED;
        }
    }

    public record ChannelIterator<T>(Channel<T> channel) implements Iterator<T> {

        public boolean hasNext() {
                return !this.channel.isEmpty();
            }

            public T next() {
                return this.channel
                        .receive()
                        .orElse(null);
            }
        }
}

