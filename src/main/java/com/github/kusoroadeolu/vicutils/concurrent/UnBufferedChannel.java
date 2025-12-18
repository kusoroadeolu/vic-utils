package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.*;

import static java.util.Objects.requireNonNull;

public class UnBufferedChannel<T> implements Channel<T> {
     final BlockingQueue<T> queue;
     final Lock modifyingLock; //Mutex lock for queue even though its thread safe.
    // It's mostly to allow threads to wait and decouple the channel's state from queue and thread semantics.
    // Might be questionable, but I believe it's clearer in the long run and prevents deadlocks and multi thread contention on a single lock
     final Lock stateLock;
     final Condition isFullCondition;
     final Condition isEmptyCondition;
     int capacity;
     State channelState;
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
            while (!this.isEmpty() || this.channelState.isNil()) {
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
            while (((val = this.queue.poll()) == null && !isClosed()) || this.channelState.isNil()){
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

    //The total cap of the queue
    public int capacity() {
        if (this.isNil()) return 0; //If the channel is nil return 0
        else return this.capacity;
    }

    //Number of T in the queue
    public int length() {
        return this.queue.size();
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
        this.stateLock.lock();
        try {
            return this.channelState.isClosed();
        }finally {
            this.stateLock.unlock();
        }
     }

    boolean isNil(){
        this.stateLock.lock();
        try {
            return this.channelState.isNil();
        }finally {
            this.stateLock.unlock();
        }
    }

     boolean isFull(){
        return this.length() >= this.capacity;
    }

     public boolean isEmpty(){
        return this.queue.isEmpty();
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

    public static class ChannelIterator<T> implements Iterator<T>{
        private final Channel<T> channel;

        public ChannelIterator(Channel<T> c){
            this.channel = c;
        }

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

