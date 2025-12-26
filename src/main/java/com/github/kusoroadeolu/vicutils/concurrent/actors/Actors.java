package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.function.Function;

public class Actors {
    private Actors(){throw new AssertionError();}

    public static <T>ActorRef<T> newActor(Function<Behaviour<T>, AbstractActor<T>> gen, Behaviour<T> behaviour){
        final AbstractActor<T> a = gen.apply(behaviour);
        a.start();
        return a;
    }

    public static <T>ActorRef<T> newActor(Function<Behaviour<T>, AbstractActor<T>> gen){
        final AbstractActor<T> a = gen.apply(Behaviour.sink());
        a.start();
        return a;
    }
}
