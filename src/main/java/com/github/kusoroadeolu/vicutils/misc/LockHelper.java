package com.github.kusoroadeolu.vicutils.misc;

import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LockHelper {
    private LockHelper() {}

    public static void withLock(Lock lock, Runnable r){
        lock.lock();
        try {
            r.run();
        }finally {
            lock.unlock();
        }
    }

    public static <T>T withLock(Lock lock, Supplier<T> sp){
        lock.lock();
        try {
            return sp.get();
        }finally {
            lock.unlock();
        }
    }

    public static <T, E>E withLock(Lock lock, T t ,Function<T, E> sp){
        lock.lock();
        try {
            return sp.apply(t);
        }finally {
            lock.unlock();
        }
    }



    public static <T>void withLock(Lock lock, T t,Consumer<T> c){
        lock.lock();
        try {
            c.accept(t);
        }finally {
            lock.unlock();
        }
    }
}
