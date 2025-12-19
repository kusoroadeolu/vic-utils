package com.github.kusoroadeolu.vicutils.concurrent.channels;

@FunctionalInterface
public interface ConsumerWithException<T> {
    void accept(T t) throws Exception;
}
