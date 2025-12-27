package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static com.github.kusoroadeolu.vicutils.concurrent.actors.ActorSystem.EXEC;

public abstract class AbstractActor<T extends Message> implements ActorRef<T>, ActorLifeCycle{

    private final MailBox<T> mailbox;
    private String address;
    private Behaviour<T> behaviour;
    private volatile boolean isTerminated;
    private String parentAddress;
    private RawActorGenerator generator;
    protected final MessageHandler<T> messageHandler;

    AbstractActor(Behaviour<T> behaviour){
        this.mailbox = new MailBox<>();
        this.address = UUID.randomUUID().toString();
        this.behaviour = behaviour;
        this.messageHandler = this.handleMessages();
        this.parentAddress = "";
        this.isTerminated = false;
    }

    public final void tell(T message) {
        this.mailbox.send(message);
    }

    public final String toString() {
        return this.address;
    }

    public final <E extends Message>ActorRef<E> spawn(Function<Behaviour<E>, AbstractActor<E>> generator, String address, String parentAddress){
        AbstractActor<E> child = ActorSystem.createAbstractActor(generator);
        child.setParentAddress(parentAddress);
        child.setAddress(address);
        child.setGenerator(generator);
        return child;
    }

    public final <E extends Message>ActorRef<E> spawn(Function<Behaviour<E>, AbstractActor<E>> generator){
        AbstractActor<E> child = ActorSystem.createAbstractActor(generator);
        child.setParentAddress(this.address);
        child.setGenerator(generator);
        return child;
    }

    @SuppressWarnings("unchecked")
    public void start(){
        EXEC.execute(() -> {
            try{
                while (!isTerminated){
                    final Optional<T> opt = this.mailbox.receive();
                    Behaviour<T> nextBehaviour;
                    if (opt.isPresent()) {
                        T val = opt.get();
                        if (this.behaviour instanceof Behaviour.Sink<T>) return; //If the behaviour is already a sink fk it

                        if (val instanceof ChildDeath(var childAddress, var pAddress ,var gen)){
                            this.spawn(
                                    (behaviour) -> (AbstractActor<Message>) gen.create(behaviour),
                                    childAddress,
                                    pAddress
                            );
                            continue;

                        }


                        nextBehaviour = this.messageHandler.get(val);
                        if (nextBehaviour != null) nextBehaviour = nextBehaviour.change(val); //Fetch the behaviour bound to this message type
                        if (!(this.behaviour instanceof Behaviour.Same<T>)) this.behaviour = nextBehaviour;
                    }
                }
            }catch (Exception e){
                if (!this.parentAddress.isBlank())
                    ActorSystem.send(this.parentAddress, new ChildDeath(this.address, this.parentAddress ,this.generator));
                this.stop();
                throw new ChildDeathException(); //Kill the thread
            }
        });
    }

    void setParentAddress(String address){
        this.parentAddress = address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    <E extends Message> void setGenerator(Function<Behaviour<E>, AbstractActor<E>> typedGenerator) {
        this.generator = (behaviour) -> {
            @SuppressWarnings("unchecked")
            var b = (Behaviour<E>) behaviour;
            return typedGenerator.apply(b);
        };
    }

    public void stop(){
        this.behaviour = Behaviour.sink();
        this.isTerminated = true;
        this.mailbox.close();
    }

    public String getParent(){
        return this.parentAddress;
    }

    public abstract MessageHandler<T> handleMessages();

    static class ActorImpl<T extends Message> extends AbstractActor<T> {
        ActorImpl(Behaviour<T> behaviour) {
            super(behaviour);
        }

        public MessageHandler<T> handleMessages() {
            return MessageHandler.<T>builder().build();
        }
    }

    record ChildDeath(String address, String parentAddress ,RawActorGenerator generator) implements Message{}
}