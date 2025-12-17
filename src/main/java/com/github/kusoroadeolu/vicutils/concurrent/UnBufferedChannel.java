package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.*;

import static java.util.Objects.requireNonNull;

public class UnBufferedChannel<T> implements Channel<T> {
     final BlockingQueue<T> queue;
     final Lock modifyingLock;
     final Lock stateLock;
     final Condition isFull;
     final Condition isEmpty;
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
        this.isFull = this.modifyingLock.newCondition();
        this.isEmpty = this.modifyingLock.newCondition();
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
                this.isFull.await();  //Block if the queue is not empty initially or the channel is nil
            }
            this.queue.add(val);

            while (!this.isEmpty()){
                this.isFull.await(); //Block again till the queue is empty
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
                this.isEmpty.await();
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } finally {
            this.modifyingLock.unlock();
        }

        return val;
    }

    //The total cap of the queue
    public int capacity() {
        return this.capacity;
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

