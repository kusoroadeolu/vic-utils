package com.github.kusoroadeolu.vicutils.concurrent.semaphore;

import com.github.kusoroadeolu.vicutils.concurrent.channels.BufferedChannel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.Channel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple semaphore but with channels instead.
 * </br> This chamaphore imposes fairness guarantees meaning FIFO for each thread
 * */
public class Chamaphore  {

    private final Channel<Thread> channel;
    private final AtomicInteger acquiredPermits;
    private final AtomicInteger parkedThreads;

    public Chamaphore(int allowed) {
        if (allowed < 1) throw new IllegalArgumentException();
        this.channel = new BufferedChannel<>(allowed);
        this.acquiredPermits = new AtomicInteger(0);
        this.parkedThreads = new AtomicInteger(0);
        this.channel.make();
    }

    public void acquire(){
        final var thread = Thread.currentThread();
        this.parkedThreads.incrementAndGet();
        this.channel.send(thread);

        this.parkedThreads.decrementAndGet();
        this.acquiredPermits.incrementAndGet();
    }

    public void release(){
        final var released = this.channel.tryReceive();
        if (released.isPresent()) this.acquiredPermits.decrementAndGet();
    }

    public int permitsAcquired(){
        return this.acquiredPermits.get();
    }

    public int parkedThreads(){
        return this.parkedThreads.get();
    }
}
