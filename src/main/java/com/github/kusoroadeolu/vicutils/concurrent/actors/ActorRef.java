package com.github.kusoroadeolu.vicutils.concurrent.actors;

public interface ActorRef<T> {
    void tell(T message);
    String toString();
}
