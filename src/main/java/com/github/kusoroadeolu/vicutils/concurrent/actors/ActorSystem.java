package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ActorSystem {
    private final static ConcurrentHashMap<String, AbstractActor<?>> MAP = new ConcurrentHashMap<>();
    private final static ActorGod ACTOR_GOD = new ActorGod(Behaviour.same());
    public final static ExecutorService EXEC = Executors.newVirtualThreadPerTaskExecutor();

    static {
        MAP.put(ACTOR_GOD.toString(), ACTOR_GOD);
    }

    private ActorSystem(){
        throw new AssertionError();
    }


    public static <T extends Message>ActorRef<T> createActor(Function<Behaviour<T>, AbstractActor<T>> generator){
        requireNonNull(generator);
        return ACTOR_GOD.spawn(generator);
    }

    static <T extends Message>AbstractActor<T> createAbstractActor(Function<Behaviour<T>, AbstractActor<T>> generator){
        requireNonNull(generator);
        final AbstractActor<T> ref = Actors.newActor(generator);
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
