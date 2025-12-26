package com.github.kusoroadeolu.vicutils.concurrent.actors;

public class AbstractPersonality<T>{
    private final Actor<T> actor;

    protected AbstractPersonality(Actor<T> actor){
        this.actor = actor;
    }

}
