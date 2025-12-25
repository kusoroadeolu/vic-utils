package com.github.kusoroadeolu.vicutils.concurrent.actors;


import java.util.Objects;

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

    public static <T>Personality<T> onMessage(Class<?> clazz, Personality<T> personality){
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(personality);
        return personality;
    }

    public static <T> Personality<T> onMessageEquals(T t, Personality<T> personality){
        Objects.requireNonNull(t);
        Objects.requireNonNull(personality);
        return personality;
    }
}
