package com.github.kusoroadeolu.vicutils.concurrent.actors;

public class Smth extends AbstractPersonality<String>{


    protected Smth(AbstractActor<String> actor) {
        super(actor);
    }

    public static Personality<String> setup(){
        return Personalities.create(Smth::new, "");
    }

}
