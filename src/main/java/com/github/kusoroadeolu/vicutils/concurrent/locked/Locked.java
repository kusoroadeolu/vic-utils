package com.github.kusoroadeolu.vicutils.concurrent.locked;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.kusoroadeolu.vicutils.misc.LockHelper.withLock;

//A class that basically locks all operations on an object
public class Locked <T>{
    private final T t;
    private final Lock lock = new ReentrantLock();

    public Locked(T t) {
        this.t = t;
    }

    public void consume(Consumer<T> consumer){
        withLock(lock, t, consumer);
    }

    public <E>E supply(Function<T, E> generator){
        return withLock(lock, t, generator);
    }


}
