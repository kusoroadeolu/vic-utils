package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface ReceiveChannel<T> extends UniDirectionalChannel<T> , Iterable<T>{
    T receive();

    @Override
    Iterator<T> iterator();

    @Override
    default Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    default void forEach(Consumer<? super T> action){
        Iterable.super.forEach(action);
    }
}
