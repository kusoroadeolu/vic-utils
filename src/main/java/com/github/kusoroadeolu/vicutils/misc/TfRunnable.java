package com.github.kusoroadeolu.vicutils.misc;

import java.util.concurrent.locks.Lock;

@FunctionalInterface
public interface TfRunnable extends Runnable{
    void run();

     static void withLock(Lock lock, Runnable r){
        lock.lock();
        try {
            r.run();
        }finally {
            lock.unlock();
        }
    }
}
