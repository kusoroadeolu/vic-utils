package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Optional;
import java.util.UUID;

public abstract class Actor<T> implements ActorRef<T>{

    private final MailBox<T> mailbox;
    private Thread thread; //A virtual thread
    private final String address;
    private final String name;
    private Personality<T> personality;
    private final MessageHandler<T> messageHandler;


    Actor(String name) {
        this.mailbox = new MailBox<>();
        this.name = name;
        this.address = UUID.randomUUID() + this.name;
        this.personality = Personalities.empty();
        this.messageHandler = new MessageHandler<>();
    }

     public static <T> ActorRef<T> create(String name){
        Actor<T> actor =  new ActorImpl<>(name);
        actor.startThread();
        return actor;
     }

    public static <T> ActorRef<T> create(String name, Personality<T> personality){
        Actor<T> actor =  new ActorImpl<>(name);
        actor.personality = personality;
        actor.startThread();
        return actor;
    }

    public void tell(T message) {
        this.mailbox.send(message);
    }

    public String toString() {
        return this.address;
    }

    private void startThread(){
        this.thread = Thread.startVirtualThread(() -> {
            while (!this.thread.isInterrupted()){
                final Optional<T> val = this.mailbox.receive();
                Personality<T> ps = null;
                if (val.isPresent()) {
                    ps = this.messageHandler.handle(val.get());
                    this.personality = ps;
                }

            }
        });
    }

    private static class ActorImpl<T> extends Actor<T>{
        private ActorImpl(String name) {
            super(name);
        }
    }


}
