package com.github.kusoroadeolu.vicutils.concurrent.actors;

public abstract class AbstractPersonality<T> implements Personality<T> {

    private final AbstractActor<T> actor;
    protected AbstractPersonality(AbstractActor<T> actor){
        this.actor = actor;
    }

    @SuppressWarnings("unchecked")
    protected void register(Object req,  Personality<T> personality){
        if (req instanceof Class<?> clazz) this.actor.messageHandler.onMessage(clazz, personality);
        else this.actor.messageHandler.onMessageEquals((T)req, personality);
    }

    public final Personality<T> change(T message) {
        return null;
    }
}
