package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractActor<T extends Message> implements ActorRef<T>, ActorLifeCycle{

    private final MailBox<T> mailbox;
    private Thread thread; //A virtual thread
    private final String address;
    private volatile Behaviour<T> behaviour;
    protected final MessageHandler<T> messageHandler;

      AbstractActor(Behaviour<T> behaviour){
         this.mailbox = new MailBox<>();
         this.address = UUID.randomUUID().toString();
         this.behaviour = behaviour;
         this.messageHandler = this.handleMessages();
      }

    public void tell(T message) {
        this.mailbox.send(message);
    }

    public String toString() {
        return this.address;
    }

    public abstract MessageHandler<T> handleMessages();

     public void start(){
        this.thread = Thread.startVirtualThread(() -> {
            while (!this.thread.isInterrupted()){
                final Optional<T> opt = this.mailbox.receive();
                Behaviour<T> nextBehaviour;
                if (opt.isPresent()) {
                    T val = opt.get();
                    if (this.behaviour instanceof Behaviour.Sink<T>) return; //If the behaviour is already a sink fk it
                    nextBehaviour = this.messageHandler.get(val)
                            .change(val);  //Fetch the behaviour bound to this message type

                    if (!(this.behaviour instanceof Behaviour.Same<T>)) this.behaviour = nextBehaviour;
                }
            }
        });
    }

    public void stop(){
         this.behaviour = Behaviour.sink();
    }

    static class ActorImpl<T extends Message> extends AbstractActor<T> {
         ActorImpl(Behaviour<T> behaviour) {
            super(behaviour);
        }

        public MessageHandler<T> handleMessages() {
            return MessageHandler.<T>builder().build();
        }
    }
}
