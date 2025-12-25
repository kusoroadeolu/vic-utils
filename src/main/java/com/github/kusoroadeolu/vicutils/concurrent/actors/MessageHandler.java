package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class MessageHandler<E> {
    private final Map<Class<?>, Personality<E>> map;
    private final static String E_NULL_MESSAGE = "clazz == null";

    private MessageHandler(Builder<E> builder){
        this.map = Collections.unmodifiableMap(builder.map);
    }

    public void run(Class<?> clazz, E message){
        requireNonNull(clazz, E_NULL_MESSAGE);
        Personality<E> e = map.get(clazz);
        e.change(message);
    }

    public static class Builder<E>{
        private final ConcurrentHashMap<Class<?>, Personality<E>> map = new ConcurrentHashMap<>();
        private final static String ACTION_NULL_MESSAGE = "action == null";

        public Builder<E> onReceive(Class<?> clazz, Personality<E> p){
            requireNonNull(clazz, E_NULL_MESSAGE);
            requireNonNull(p, ACTION_NULL_MESSAGE);
            map.put(clazz, p);
            return this;
        }

        public MessageHandler<E> build(){
            return new MessageHandler<>(this);
        }
    }
}
