package com.github.kusoroadeolu.vicutils.concurrent.actors;


//This class is the root of all actors, but can only receive child death messages
final class ActorGod extends AbstractActor<AbstractActor.ChildDeath>{

    ActorGod(Behaviour<ChildDeath> behaviour) {
        super(behaviour);
    }


    public MessageHandler<ChildDeath> handleMessages() {
        return MessageHandler.<ChildDeath>builder().build();
    }


}
