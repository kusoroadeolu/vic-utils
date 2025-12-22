package com.github.kusoroadeolu.vicutils.misc;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

@FunctionalInterface
public interface TfSupplier<T> extends Supplier<T> {
    T get();

    static <T>T withLock(Lock lock, Supplier<T> sp){
        lock.lock();
        try {
            return sp.get();
        }finally {
            lock.unlock();
        }
    }
}
