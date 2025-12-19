package com.github.kusoroadeolu.vicutils.concurrent.channels;

public interface SendChannel<T> extends UniDirectionalChannel<T>{
    void send(T val);
    boolean trySend(T val);
    void close();
}
