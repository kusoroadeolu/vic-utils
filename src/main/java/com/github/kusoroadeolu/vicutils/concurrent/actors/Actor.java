package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Optional;

public class Actor<T> implements ActorRef<T>{
    private final T t;
    private final MailBox<T> mailbox;
    private Thread thread; //A virtual thread
    private final String address;

    public Actor(T t, String address) {
        this.t = t;
        this.mailbox = new MailBox<>();
        this.address = address;
        this.startThread();
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
                Optional<T> val = this.mailbox.receive();// TODO Handle action here
            }
        });
    }

}
