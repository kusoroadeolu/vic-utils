package com.github.kusoroadeolu.vicutils.concurrent.actors;


//This class is the root of all actors, but can only receive child death messages
final class SystemActor extends AbstractActor<AbstractActor.ChildDeath>{

    SystemActor(Behaviour<ChildDeath> behaviour) {
        super(behaviour);
        this.setAddress("SYSTEM ACTOR");
    }

    public MessageHandler<ChildDeath> handleMessages() {
        return MessageHandler.<ChildDeath>builder().build();
    }


}
