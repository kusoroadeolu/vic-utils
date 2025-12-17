package com.github.kusoroadeolu.vicutils.concurrent;

// A dummy interface to represent if a channel is unidirectional
public interface UniDirectionalChannel<T>{
    int capacity();
    int length();
    void make();
}
