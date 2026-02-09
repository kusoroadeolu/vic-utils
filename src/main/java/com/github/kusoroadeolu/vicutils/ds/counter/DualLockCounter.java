package com.github.kusoroadeolu.vicutils.ds.counter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
//Mainly for learning not to compete with LongAdder. Any comments on that and i wont take you seriously
public class DualLockCounter implements Counter{
    private final Lock incrLock;
    private final Lock decrLock;
    private long incr;
    private long decr;

    public DualLockCounter() {
        this.incrLock = new ReentrantLock();
        this.decrLock = new ReentrantLock();
    }

    @Override
    public void increment() {
        this.increment(1);
    }

    @Override
    public void decrement() {
        this.decrement(1);
    }

    @Override
    public void increment(long count) {
        if (count < 1) throw new IllegalArgumentException("count < 1");
        this.incrLock.lock();
        try {
            incr+=count;
        }finally {
            this.incrLock.unlock();
        }
    }

    @Override
    public void decrement(long count) {
        if (count < 1) throw new IllegalArgumentException("count < 1");
        this.decrLock.lock();
        try {
            decr+=count;
        }finally {
            this.decrLock.unlock();
        }
    }

    @Override
    public long get() {
        this.incrLock.lock();
        this.decrLock.lock();
        try {
            return incr - decr;
        }finally {
            this.decrLock.unlock();
            this.incrLock.unlock();
        }
    }

    public void reset() {
        this.incrLock.lock();
        this.decrLock.lock();
        try {
            incr = 0;
            decr = 0;
        }finally {
            this.decrLock.unlock();
            this.incrLock.unlock();
        }
    }
}
