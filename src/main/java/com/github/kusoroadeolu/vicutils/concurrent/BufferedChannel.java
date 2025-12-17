package com.github.kusoroadeolu.vicutils.concurrent;

import static java.util.Objects.requireNonNull;

public class BufferedChannel<T> extends UnBufferedChannel<T> {
    public BufferedChannel(int capacity){
        super();
        this.capacity = capacity;
    }


    public void send(T val){
        requireNonNull(val);
        this.verifyIfClosed();
        this.modifyingLock.lock();
        try {
            while (this.isFull() || this.isNil()) {
                this.isFullCondition.await();  //Block if the queue is full initially
            }

            this.queue.add(val); //Don't block after
            this.isEmptyCondition.signal();
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
