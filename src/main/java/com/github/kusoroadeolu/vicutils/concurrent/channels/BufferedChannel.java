package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

import static java.util.Objects.requireNonNull;

public class BufferedChannel<T> extends UnBufferedChannel<T> {
    public BufferedChannel(int capacity){
        super();
        this.capacity = capacity;
        this.buf = new ArrayList<>(this.capacity);

    }

    public void send(T val){
        requireNonNull(val);
        this.verifyIfClosed();
        this.channelLock.lock();
        try {
            while (this.isFull() || this.isNil()) {
                this.isFullCondition.await();  //Block if the queue is full initially
            }

            this.buf.addLast(val); //Don't block after
            this.isEmptyCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            this.channelLock.unlock();
        }
    }
}
