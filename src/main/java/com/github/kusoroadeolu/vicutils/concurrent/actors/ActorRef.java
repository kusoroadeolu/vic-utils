package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.function.Function;

public interface ActorRef<T> {
    boolean tell(T message);
    String toString();

    <E extends Message>ActorRef<E> spawn(Function<Behaviour<E>, AbstractActor<E>> generator, String childAddress);

    <E extends Message>ActorRef<E> spawn(Function<Behaviour<E>, AbstractActor<E>> generator);
    String getParent();
}
