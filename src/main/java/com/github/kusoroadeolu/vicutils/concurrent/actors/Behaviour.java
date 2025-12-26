package com.github.kusoroadeolu.vicutils.concurrent.actors;

@FunctionalInterface
public interface Behaviour<T> {
    static Behaviour<?> EMPTY = _ -> null;
    Behaviour<T> change(T message);

    @SuppressWarnings("unchecked")
    static <T>Behaviour<T> empty(){
        return (Behaviour<T>) EMPTY;
    }
}
