package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;

public class UnBufferedChannel<T> implements Channel<T> {
     ArrayList<T> buf;
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
        this.buf = new ArrayList<>(this.capacity);
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
        this.verifyIfNil();
        this.verifyIfClosed();
        this.channelLock.lock();
        try {
            this.verifyIfClosed();
            while (!this.isEmpty() || this.isNil()) {
                verifyIfClosed();
                this.isFullCondition.await();  //Block indefinitely if the queue is not empty initially or the channel is nil
            }

            this.buf.add(val);
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
        if (this.isClosed()) return this.fallbackNull(this.buf.getFirst());
        this.channelLock.lock();
        T val = null;
        try {
            while (((val = this.buf.removeFirst()) == null && !isClosed()) || this.isNil()){
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
        this.verifyIfNil();
        this.channelLock.lock();
        boolean bool;
        try {
            this.verifyIfClosed();
            this.verifyIfNil();
            bool = this.buf.add(val);
            if (bool) this.isEmptyCondition.signalAll();
        }finally {
            this.channelLock.unlock();
        }

        return bool;
    }

    public Optional<T> tryReceive() {
        if (this.isNil()) return Optional.empty();
        this.channelLock.lock();
        T t;
        try {
            if (this.isNil()) return Optional.empty();
            t = this.buf.getFirst();
            if (t != null) this.isFullCondition.signalAll();
        } finally {
            this.channelLock.unlock();
        }

        return this.fallbackNull(t);
    }

    //The total cap of the buffer
    public int capacity() {
        if (this.isNil()) return 0; //If the channel is nil return 0
        else return this.capacity;
    }

    //Number of T in the buffer
    //Weakly consistent
    public int length() {
        return this.buf.size();
    }

    public boolean ok() {
        return !this.isClosed() && !this.isNil();
    }

    public void close(){
        this.verifyIfNil();
        this.verifyIfClosed();
        this.channelLock.lock();
        try {
            this.verifyIfNil();
            this.verifyIfClosed();
            this.channelState = State.CLOSED;
            this.isEmptyCondition.signalAll();
            this.isFullCondition.signalAll();
        }finally {
            this.channelLock.unlock();
        }
    }

     //Weakly consistent
     public boolean isEmpty(){
        return this.buf.isEmpty();
     }

     Optional<T> fallbackNull(T t){
         return t == null ? Optional.empty() : Optional.of(t);
     }

    //Helper method for Channel#receive to allow users to drain the channel after close
    void verifyIfClosed(){
        if (this.isClosed()) throw new ChannelClosedException(CHANNEL_CLOSED_MESSAGE);
    }

    void verifyIfNil(){
        if (this.isNil()) throw new ChannelNilException(CHANNEL_NIL_MESSAGE);
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

        boolean isClosed(){
            return this == CLOSED;
        }
    }

}

