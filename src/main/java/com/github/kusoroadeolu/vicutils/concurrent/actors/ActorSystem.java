package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ActorSystem {
    private final static ConcurrentHashMap<String, AbstractActor<?>> MAP = new ConcurrentHashMap<>();
    private final static SystemActor ACTOR_SYSTEM = new SystemActor(Behaviour.same());
    public final static ExecutorService EXEC = Executors.newVirtualThreadPerTaskExecutor();

    static {
        MAP.put(ACTOR_SYSTEM.toString(), ACTOR_SYSTEM);
    }

    private ActorSystem(){
        throw new AssertionError("No instance for you :)");
    }


    public static <T extends Message>ActorRef<T> createActor(Function<Behaviour<T>, AbstractActor<T>> generator){
        requireNonNull(generator);
        return ACTOR_SYSTEM.spawn(generator);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Message>void send(String address, T message){
        requireNonNull(address);
        requireNonNull(message);
        ActorRef<T> ref = (ActorRef<T>) MAP.get(address);
        if (ref == null) throw new NullPointerException("Address does not exist");
        else ref.tell(message);
    }

    public static void close(){
        ACTOR_SYSTEM.stop();
        EXEC.close();
    }

    static <T extends Message>AbstractActor<T> createAbstractActor(Function<Behaviour<T>, AbstractActor<T>> generator){
        requireNonNull(generator);
        final AbstractActor<T> ref = Actors.newAbstractActor(generator);
        MAP.put(ref.toString(), ref);
        return ref;
    }

    //Helper method
    static <T extends Message>AbstractActor<T> registerActor(AbstractActor<T> actor){
        MAP.put(actor.toString(), actor);
        return actor;
    }

    static void remove(String address){
        requireNonNull(address);
        MAP.remove(address);
    }



}
