package com.github.kusoroadeolu.vicutils.concurrent.semaphore;

public class Barrier {
    private int limit;
    private int generation;
    private final int cap;
    private final Object lock;

    public Barrier(int limit) {
        if (limit < 1) throw new IllegalArgumentException();
        this.limit = limit;
        this.cap = limit;
        this.generation = 0;
        this.lock = new Object();
    }

    public void await() throws InterruptedException {
        synchronized (this.lock){
            if(--this.limit < 0) throw new IllegalArgumentException();
            else if (this.limit == 0) {
                ++this.generation;
                this.lock.notifyAll();
            }
            else{
                int generation = this.generation;
                while (this.limit != 0 && generation == this.generation){
                    this.lock.wait();
                }
            }
        }
    }


    public void reset(){
        synchronized (this.lock){
            if (this.limit != 0) throw new IllegalArgumentException();
            this.limit = this.cap;
        }
    }
}

