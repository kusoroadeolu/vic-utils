package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MessageHandler<E> {
    private final Map<Object, Personality<E>> map;
    private final static String CLAZZ_NULL_MESSAGE = "clazz == null";
    private final static String OBJ_NULL_MESSAGE = "obj == null";
    private final static String PERSONALITY_NULL_MESSAGE = "personality == null";



    public MessageHandler(){
        this.map = new HashMap<>();
    }


    public void onMessage(Class<?> clazz, Personality<E> p){
        requireNonNull(clazz, CLAZZ_NULL_MESSAGE);
        requireNonNull(p, PERSONALITY_NULL_MESSAGE);
        this.map.put(clazz, p);
    }

    public void onMessageEquals(E e, Personality<E> p){
        requireNonNull(e, OBJ_NULL_MESSAGE);
        requireNonNull(p, PERSONALITY_NULL_MESSAGE);
        this.map.put(e, p);
    }

    public Personality<E> handle(E message){
        requireNonNull(message, OBJ_NULL_MESSAGE);
        final Personality<E> e = map.get(message);
        e.change(message);
        return e;
    }

}
