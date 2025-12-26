package com.github.kusoroadeolu.vicutils.concurrent.actors;


import java.util.Objects;
import java.util.function.Function;

public class Personalities {
    private final static Personality<?> EMPTY = _ -> null;
    private final static Personality<?> SAME = _ -> null;

    private Personalities() {
        throw new AssertionError("No instance for you :)");
    }

    @SuppressWarnings("unchecked")

    public static <T>Personality<T> empty(){
        return _ -> (Personality<T>) EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static <T>Personality<T> same(){
        return   _ -> (Personality<T>) SAME;
    }


    public static <T>Personality<T> create(Function<AbstractActor<T>, ? extends Personality<T>> gen, String name){
        var a = AbstractActor.<T>newActor(name);
        return gen.apply(a);
    }
}
