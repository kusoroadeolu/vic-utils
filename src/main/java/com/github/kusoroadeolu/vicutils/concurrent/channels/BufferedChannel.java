package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.ArrayDeque;
import java.util.Optional;

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
        this.lock.lock();
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
            this.lock.unlock();
        }
    }

    public Optional<T> receive() {
        if (this.isClosed()) return Optional.ofNullable(this.buf.poll());
        this.lock.lock();
        T val;
        try {
            while (((val = this.buf.poll()) == null && !isClosed()) || this.isNil()){
                //Block indefinitely if the channel does not have value and is not closed or the channel is NIL.
                // Awaken only if the channel has closed or a new value arrived
                this.canReceive.awaitUninterruptibly();

            }

            this.canSend.signal();

        } finally {
            this.lock.unlock();
        }

        return Optional.ofNullable(val);
    }

    public Optional<T> tryReceive() {
        if (this.isNil()) return Optional.empty();
        this.lock.lock();
        T t;
        try {
            if (this.isNil()) return Optional.empty();
            t = this.buf.poll();
            if (t != null) {
                this.canSend.signal();
            }

        } finally {
            this.lock.unlock();
        }

        return Optional.ofNullable(t);
    }
}
