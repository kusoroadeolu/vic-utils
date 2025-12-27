package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ActorSystem {
    private static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
    private final ConcurrentHashMap<String, AbstractActor<?>> actorRegistry = new ConcurrentHashMap<>();
    private final SystemActor systemActor = new SystemActor(Behaviour.same());

    private ActorSystem(){
        actorRegistry.put(systemActor.toString(), systemActor);
        systemActor.start();
    }

    public static ActorSystem getContext(){
        return ActorSystemHolder.CONTEXT;
    }

    public <T extends Message>ActorRef<T> createActor(Function<Behaviour<T>, AbstractActor<T>> generator){
        requireNonNull(generator);
        return systemActor.spawn(generator);
    }

    public <T extends Message>ActorRef<T> createActor(Function<Behaviour<T>, AbstractActor<T>> generator, String address){
        requireNonNull(generator);
        requireNonNull(address);
        return systemActor.spawn(generator, address);
    }


    //For tests
    @SuppressWarnings("unchecked")
    public  <T extends Message>ActorRef<T> getActor(String address){
        requireNonNull(address);
        return (ActorRef<T>) actorRegistry.get(address);
     }

    @SuppressWarnings("unchecked")
    public <T extends Message>void send(String address, T message){
        requireNonNull(address);
        requireNonNull(message);
        ActorRef<T> ref = (ActorRef<T>) actorRegistry.get(address);
        if (ref == null) throw new NullPointerException("Address does not exist");
        else ref.tell(message);
    }

    public void close(){
        systemActor.stop();
        EXECUTOR.close();
    }


    //Helper method
     <T extends Message>AbstractActor<T> registerActor(AbstractActor<T> actor){
        actorRegistry.putIfAbsent(actor.toString(), actor);
        return actor;
     }

     void remove(String address){
        requireNonNull(address);
        actorRegistry.remove(address);
     }

     int size(){
        return actorRegistry.size();
     }

     static ExecutorService executor() {
        return EXECUTOR;
     }

    static class ActorSystemHolder{
        private final static ActorSystem CONTEXT = new ActorSystem();
    }
}
