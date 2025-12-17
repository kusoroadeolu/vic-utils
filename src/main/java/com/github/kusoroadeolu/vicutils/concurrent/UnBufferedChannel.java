package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.*;

import static java.util.Objects.requireNonNull;

public class UnBufferedChannel<T> implements BiDirectionalChannel<T>{
     BlockingQueue<T> queue;
     Lock modifyingLock;
     Lock stateLock;
     Condition isFull;
     Condition isEmpty;
     int capacity;
     State channelState;
     private final static int MAX_CAPACITY = 1;

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
        } catch (InterruptedException e) {
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
            while (((val = this.queue.poll()) == null && !isClosed()) || this.channelState.isNil()){ //Block indefinitely if the channel does not have value and is not closed or the channel is NIL
                this.isEmpty.await();
            }
        } catch (InterruptedException e) {
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


    public void close(){
        this.stateLock.lock();
        try {
            if (this.isNil()) throw new ChannelNilException("This channel is still NIL");
            this.verifyIfClosed();
            this.channelState = State.CLOSED;
        }finally {
            this.stateLock.unlock();
        }
    }

    //Helper method for Channel#receive to allow users to drain the channel after close
    private void verifyClosedAndEmpty(){
        this.stateLock.lock();
        try {
            if (this.isClosed() && isEmpty()) throw new ChannelClosedException("Channel is already closed");
        }finally {
            this.stateLock.unlock();
        }
    }

    void verifyIfClosed(){
        this.stateLock.lock();
        try {
            if (this.isClosed()) throw new ChannelClosedException("Channel is already closed");
        }finally {
            this.stateLock.unlock();
        }
    }

     boolean isClosed(){
        return this.channelState.isClosed();
     }

    boolean isNil(){
        return this.channelState.isNil();
    }

     boolean isFull(){
        return (this.length() + this.queue.remainingCapacity()) >= this.capacity;
    }

    //Number of T in the queue which haven't been removed
    public int length() {
        final var rem = this.queue.remainingCapacity(); //Number of T the queue can accept before throwing
        return this.capacity - rem;
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

