package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MessageHandler<E> {
    private final Map<Object, Behaviour<E>> map;
    private final static String CLAZZ_NULL_MESSAGE = "clazz == null";
    private final static String OBJ_NULL_MESSAGE = "obj == null";
    private final static String PERSONALITY_NULL_MESSAGE = "personality == null";

    MessageHandler(Builder<E> builder){
        this.map = Collections.unmodifiableMap(builder.map);
    }

    public static <E>Builder<E> builder(){
        return new Builder<>();
    }

    public Behaviour<E> get(Object o){
        requireNonNull(o, OBJ_NULL_MESSAGE);
        var b = this.map.get(o);
        return b  == null ? Behaviour.sink() : b;
    }

    public static class Builder<E>{
        private final Map<Object, Behaviour<E>> map = new HashMap<>();
        public Builder<E> onMessage(Class<?> clazz, Behaviour<E> p){
            requireNonNull(clazz, CLAZZ_NULL_MESSAGE);
            requireNonNull(p, PERSONALITY_NULL_MESSAGE);
            this.map.put(clazz, p);
            return this;
        }

        public Builder<E> onMessageEquals(E e, Behaviour<E> p){
            requireNonNull(e, OBJ_NULL_MESSAGE);
            requireNonNull(p, PERSONALITY_NULL_MESSAGE);
            this.map.put(e, p);
            return this;
        }

        public MessageHandler<E> build(){
            return new MessageHandler<>(this);
        }
    }

}
