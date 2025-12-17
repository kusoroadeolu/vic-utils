package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.Iterator;
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

    public T receive() {
        this.verifyClosedAndEmpty();
        this.modifyingLock.lock();
        T val = null;
        try {
            while (((val = this.queue.poll()) == null && !isClosed()) || this.channelState.isNil()){
                //Block indefinitely if the channel does not have value and is not closed or the channel is NIL
                this.isEmptyCondition.await();
            }

            this.isFullCondition.signal();
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } finally {
            this.modifyingLock.unlock();
        }

        return val;
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


    public void close(){
        this.stateLock.lock();
        try {
            if (this.isNil()) throw new ChannelNilException(CHANNEL_NIL_MESSAGE);
            this.verifyIfClosed();
            this.channelState = State.CLOSED;
        }finally {
            this.stateLock.unlock();
        }
    }

    //Helper method for Channel#receive to allow users to drain the channel after close
    private void verifyClosedAndEmpty(){
        if (this.isClosed() && isEmpty()) throw new ChannelClosedException(CHANNEL_CLOSED_MESSAGE);
    }

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



     boolean isEmpty(){
        return this.queue.isEmpty();
    }

    public Iterator<T> iterator() {
        return this.queue.iterator();
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
}

