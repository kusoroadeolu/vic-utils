package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ActorSystem {
    private final static ConcurrentHashMap<String, AbstractActor<?>> MAP = new ConcurrentHashMap<>();

    private ActorSystem(){
        throw new AssertionError();
    }

    public static <T extends Message>ActorRef<T> createActor(Function<Behaviour<T>, AbstractActor<T>> generator){
        requireNonNull(generator);
        final AbstractActor<T> ref = Actors.newActor(generator);
        MAP.put(ref.toString(), ref);
        return ref;
    }

    public static <T extends Message>ActorRef<T> createActor(Function<Behaviour<T>, AbstractActor<T>> generator, Behaviour<T> behaviour){
        requireNonNull(generator);
        final AbstractActor<T> ref = Actors.newActor(generator, behaviour);
        MAP.put(ref.toString(), ref);
        return ref;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Message>void send(String address, T message){
        requireNonNull(address);
        requireNonNull(message);
        ActorRef<T> ref = (ActorRef<T>) MAP.get(address);
        if (ref == null) throw new NullPointerException("Address does not exist");
        else ref.tell(message);
    }


}
