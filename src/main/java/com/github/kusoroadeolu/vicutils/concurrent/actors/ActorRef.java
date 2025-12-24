package com.github.kusoroadeolu.vicutils.concurrent.actors;

public interface ActorRef<T> {
    void tell(Message<T> message);
    String getAddress();
}
