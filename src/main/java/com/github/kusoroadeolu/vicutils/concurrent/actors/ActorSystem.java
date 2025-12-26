package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ActorSystem {
    private final static ConcurrentHashMap<String, ActorRef<?>> MAP = new ConcurrentHashMap<>();

    private ActorSystem(){
        throw new AssertionError();
    }

    public static <T>ActorRef<T> createActor(Function<Behaviour<T>, AbstractActor<T>> generator){
        requireNonNull(generator);
        final ActorRef<T> ref = Actors.newActor(generator);
        MAP.put(ref.toString(), ref);
        return ref;
    }

    public static <T>ActorRef<T> createActor(Function<Behaviour<T>, AbstractActor<T>> generator, Behaviour<T> behaviour){
        requireNonNull(generator);
        final ActorRef<T> ref = Actors.newActor(generator, behaviour);
        MAP.put(ref.toString(), ref);
        return ref;
    }


}
