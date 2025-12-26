package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.*;
import java.util.function.Function;

public abstract class AbstractActor<T extends Message> implements ActorRef<T>, ActorLifeCycle{

    private final MailBox<T> mailbox;
    private Thread thread; //A virtual thread, will change this to an executor soon
    private String address;
    private Behaviour<T> behaviour;
    private volatile boolean isTerminated;
    private String parentAddress;
    private final Map<String, ActorLifeCycle> children;
    private Function<Behaviour<T>, AbstractActor<T>> generator;
    protected final MessageHandler<T> messageHandler;

      AbstractActor(Behaviour<T> behaviour){
         this.mailbox = new MailBox<>();
         this.address = UUID.randomUUID().toString();
         this.behaviour = behaviour;
         this.children = new HashMap<>();
         this.messageHandler = this.handleMessages();
         this.parentAddress = "";
         this.isTerminated = false;
      }

    public void tell(T message) {
        this.mailbox.send(message);
    }

    public String toString() {
        return this.address;
    }

    public <E extends Message>ActorRef<E> spawn(Function<Behaviour<E>, AbstractActor<E>> generator, String address){
       AbstractActor<E> child = (AbstractActor<E>) ActorSystem.createActor(generator);
       this.children.put(child.toString(), child);
       child.setParentAddress(this.address);
       child.setAddress(address);
       return child;
    }

    public <E extends Message>ActorRef<E> spawn(Function<Behaviour<E>, AbstractActor<E>> generator){
        AbstractActor<E> child = (AbstractActor<E>) ActorSystem.createActor(generator);
        this.children.put(child.toString(), child);
        child.setParentAddress(this.address);
        return child;
    }

    public abstract MessageHandler<T> handleMessages();

     public void start(){
        this.thread = Thread.startVirtualThread(() -> {
            try{
                while (!isTerminated){
                    final Optional<T> opt = this.mailbox.receive();
                    Behaviour<T> nextBehaviour;
                    if (opt.isPresent()) {
                        T val = opt.get();
                        if (this.behaviour instanceof Behaviour.Sink<T>) return; //If the behaviour is already a sink fk it
                        if (val instanceof ChildDeath(String childAddress, Function<Behaviour<Message>, AbstractActor<Message>> generator)){
                            this.children.get(childAddress).stop(); //Stop the child actor
                            this.spawn(generator, childAddress);
                        }

                        nextBehaviour = this.messageHandler.get(val);
                        if (nextBehaviour != null) nextBehaviour = nextBehaviour.change(val); //Fetch the behaviour bound to this message type
                        if (!(this.behaviour instanceof Behaviour.Same<T>)) this.behaviour = nextBehaviour;
                    }
                }
            }catch (Exception e){
                if (!this.parentAddress.isBlank()) ActorSystem.send(this.parentAddress, new ChildDeath<T>(this.address, this.generator));
                throw new ChildDeathException(); //Kill the thread
            }
        });
    }


    void setParentAddress(String address){
         this.parentAddress = address;
    }

    void setGenerator(Function<Behaviour<T>, AbstractActor<T>> generator) {
        this.generator = generator;
    }

    void setAddress(String address) {
        this.address = address;
    }

    public void stop(){
         this.behaviour = Behaviour.sink();
         this.isTerminated = true;
         this.mailbox.close();
    }

    static class ActorImpl<T extends Message> extends AbstractActor<T> {
         ActorImpl(Behaviour<T> behaviour) {
            super(behaviour);
        }

        public MessageHandler<T> handleMessages() {
            return MessageHandler.<T>builder().build();
        }
    }

    record ChildDeath<T extends Message>(String address, Function<Behaviour<T>, AbstractActor<T>> generator) implements Message{}
}
