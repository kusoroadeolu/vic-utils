package com.github.kusoroadeolu.vicutils.concurrent.actors;

@FunctionalInterface
public interface RawActorGenerator {
    AbstractActor<? extends Message> create(Behaviour<? extends Message> behaviour);

}
