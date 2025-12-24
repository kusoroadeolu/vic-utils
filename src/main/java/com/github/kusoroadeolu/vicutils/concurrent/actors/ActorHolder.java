package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class ActorHolder {
    private final static Map<String, Actor<?>> ACTOR_MAP = new ConcurrentHashMap<>();

    public ActorRef<?> spawn(String address, Object o){
        requireNonNull(o);
        Actor<?> actor = new Actor<>(o, address);
        ACTOR_MAP.put(address, actor);
        return actor;
    }
}
