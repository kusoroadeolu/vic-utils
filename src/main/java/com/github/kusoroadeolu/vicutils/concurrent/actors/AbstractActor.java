package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractActor<T> implements ActorRef<T>{

    private final MailBox<T> mailbox;
    private Thread thread; //A virtual thread
    private final String address;
    private final String name;
    private Personality<T> personality;
    protected final MessageHandler<T> messageHandler;


    AbstractActor(String name) {
        this.mailbox = new MailBox<>();
        this.name = name;
        this.address = UUID.randomUUID() + this.name;
        this.personality = Personalities.empty();
        this.messageHandler = new MessageHandler<>();
    }

    public void tell(T message) {
        this.mailbox.send(message);
    }

    public String toString() {
        return this.address;
    }

    public static <T>AbstractActor<T> newActor(String name){
        final AbstractActor<T> a = new AbstractActor.ActorImpl<>(name);
        a.startThread();
        return a;
    }

    private void startThread(){
        this.thread = Thread.startVirtualThread(() -> {
            while (!this.thread.isInterrupted()){
                final Optional<T> val = this.mailbox.receive();
                Personality<T> ps;
                if (val.isPresent()) {
                    ps = this.messageHandler.handle(val.get());
                    if (ps == null) this.personality = Personalities.same();
                    else this.personality = ps;
                }
            }
        });
    }

    public static class ActorImpl<T> extends AbstractActor<T> {
         ActorImpl(String name) {
            super(name);
        }
    }
}
