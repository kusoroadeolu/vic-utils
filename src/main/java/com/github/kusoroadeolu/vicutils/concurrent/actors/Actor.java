package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Actor<T> implements ActorRef<T>{
    private final T t;
    private final MailBox<Consumer<T>> mailbox;
    private Thread thread; //A virtual thread
    private final String address;
    private List<Actor<?>> children;

    public Actor(T t, String address) {
        this.t = t;
        this.mailbox = new MailBox<>();
        this.address = address;
        this.startThread();
    }

    @Override
    public void tell(Message<T> message) {
        this.mailbox.send(message.message());
    }

    public String getAddress() {
        return this.address;
    }

    protected void add(Actor<?> actor){
        this.children.add(actor);
    }

    private void startThread(){
        this.thread = Thread.startVirtualThread(() -> {
            while (!this.thread.isInterrupted()){
                Optional<Consumer<T>> val = this.mailbox.receive();
                val.ifPresent(consumer -> consumer.accept(t));
            }
        });
    }

}
