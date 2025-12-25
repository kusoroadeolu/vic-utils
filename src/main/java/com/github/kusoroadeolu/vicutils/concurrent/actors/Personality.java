package com.github.kusoroadeolu.vicutils.concurrent.actors;

@FunctionalInterface
public interface Personality<T> {
    Personality<T> change(T message);
}
