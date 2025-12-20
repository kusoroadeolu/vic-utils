package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;

public class UnBufferedChannel<T> implements Channel<T> {
    BlockingQueue<T> queue;
     final Lock channelLock;
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
        this.channelLock = new ReentrantLock();
        this.isFullCondition = this.channelLock.newCondition();
        this.isEmptyCondition = this.channelLock.newCondition();
        this.channelState = State.NIL;
    }

    public void make(){
        verifyIfClosed();
        this.channelLock.lock();
        try {
            verifyIfClosed();
            if(this.isNil()) this.channelState = State.OPEN;
        }finally {
            this.channelLock.unlock();
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
        this.channelLock.lock();
        try {
            while (!this.isEmpty() || this.isNil()) {
                verifyIfClosed();
                this.isFullCondition.await();  //Block indefinitely if the queue is not empty initially or the channel is nil
            }

            this.queue.add(val);
            this.isEmptyCondition.signalAll();

            while (!this.isEmpty()){
                verifyIfClosed();
                this.isFullCondition.await(); //Block again till the queue is empty
            }

        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } finally {
            this.channelLock.unlock();
        }
    }

    public Optional<T> receive() {
        if (this.isClosed()) return this.fallbackNull(this.queue.poll());
        this.channelLock.lock();
        T val = null;
        try {
            while (((val = this.queue.poll()) == null && !isClosed()) || this.isNil()){
                //Block indefinitely if the channel does not have value and is not closed or the channel is NIL.
                // Awaken only if the channel has closed or a new value arrived
                this.isEmptyCondition.await();
            }

            this.isFullCondition.signalAll();
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } finally {
            this.channelLock.unlock();
        }

        return this.fallbackNull(val);
    }


    public boolean trySend(T val) {
        requireNonNull(val);
        this.verifyIfClosed();
        return this.queue.offer(val);
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

    public boolean ok() {
        return !this.isClosed() && !this.isNil();
    }

    public void close(){
        this.channelLock.lock();
        try {
            if (this.isNil()) throw new ChannelNilException(CHANNEL_NIL_MESSAGE);
            this.verifyIfClosed();
            this.channelState = State.CLOSED;
            this.isEmptyCondition.signalAll();
            this.isFullCondition.signalAll();

        }finally {
            this.channelLock.unlock();
        }
    }

     public boolean isEmpty(){
        return this.queue.isEmpty();
     }

     Optional<T> fallbackNull(T t){
         return t == null ? Optional.empty() : Optional.of(t);
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

}

