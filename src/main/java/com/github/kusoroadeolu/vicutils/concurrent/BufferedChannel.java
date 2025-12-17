package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;

public class BufferedChannel<T> extends UnBufferedChannel<T> {
    public BufferedChannel(int capacity){
        this.capacity = capacity;
        this.queue = new ArrayBlockingQueue<>(this.capacity);
        this.modifyingLock = new ReentrantLock();
        this.stateLock = new ReentrantLock();
        this.isFull = this.modifyingLock.newCondition();
        this.isEmpty = this.modifyingLock.newCondition();
        this.channelState = State.OPEN;
    }


    public void send(T val){
        requireNonNull(val);
        this.verifyIfClosed();
        this.modifyingLock.lock();
        try {
            while (this.isFull() || this.isNil()) {
                this.isFull.await();  //Block if the queue is full initially
            }

            this.queue.add(val); //Don't block after
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            this.modifyingLock.unlock();
        }
    }

    @Override
    public T receive() {
        return super.receive();
    }
}
