package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

import static java.util.Objects.requireNonNull;

public class BufferedChannel<T> extends UnBufferedChannel<T> {
    public BufferedChannel(int capacity){
        super();
        this.capacity = capacity;
        this.queue = new ArrayBlockingQueue<>(this.capacity);

    }

    public void send(T val){
        requireNonNull(val);
        this.verifyIfClosed();
        this.channelLock.lock();
        try {
            while (this.isFull() || this.isNil()) {
                this.isFullCondition.await();  //Block if the queue is full initially
            }

            this.queue.add(val); //Don't block after
            this.isEmptyCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            this.channelLock.unlock();
        }
    }

    public Optional<T> receive() {
        return super.receive();
    }
}
