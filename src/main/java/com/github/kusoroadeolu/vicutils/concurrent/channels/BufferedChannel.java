package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

import static java.util.Objects.requireNonNull;

public class BufferedChannel<T> extends UnBufferedChannel<T> {
    public BufferedChannel(int capacity){
        super();
        this.capacity = capacity;
        this.buf = new ArrayDeque<>(this.capacity);

    }

    public void send(T val){
        requireNonNull(val);
        this.verifyIfNil();
        this.verifyIfClosed();
        this.channelLock.lock();
        try {
            this.verifyIfClosed();
            while (this.isFull() || this.isNil()) {
                this.verifyIfClosed();
                this.canSend.await();  //Block if the queue is full initially
            }

            this.buf.add(val); //Don't block after
            this.canReceive.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            this.channelLock.unlock();
        }
    }
}
