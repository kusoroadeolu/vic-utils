package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.function.Function;

public class Actors {
    private Actors(){throw new AssertionError();}

     static <T extends Message>AbstractActor<T> newAbstractActor(Function<Behaviour<T>, AbstractActor<T>> gen, Behaviour<T> behaviour){
        final AbstractActor<T> a = gen.apply(behaviour);
        a.setGenerator(gen);
        a.start();
        return a;
     }

     static <T extends Message>AbstractActor<T> newAbstractActor(Function<Behaviour<T>, AbstractActor<T>> gen){
        final AbstractActor<T> a = gen.apply(Behaviour.same());
        a.setGenerator(gen);
        a.start();
        return a;
     }

}
