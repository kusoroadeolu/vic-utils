package com.github.kusoroadeolu.vicutils.concurrent.actors;

class Smth extends AbstractPersonality<String>{


    protected Smth(AbstractActor<String> actor) {
        super(actor);
    }

    public static Personality<String> setup(){
        return Personalities.<String>create(Smth::new, "");
    }

}
