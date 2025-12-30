package com.github.kusoroadeolu.vicutils.concurrent.actors;

import com.github.kusoroadeolu.vicutils.concurrent.actors.exceptions.ChildDeathException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static com.github.kusoroadeolu.vicutils.concurrent.actors.ActorSystem.getContext;
import static com.github.kusoroadeolu.vicutils.misc.Try.run;

public abstract class AbstractActor<T extends Message> implements ActorRef<T>, ActorLifeCycle {

    protected final MessageHandler<T> messageHandler;
    private final MailBox<T> mailbox;
    private final List<ActorMetadata> children;
    private String address;
    private Behaviour<T> behaviour;
    private volatile boolean isTerminated;
    private String parentAddress;
    private RawActorGenerator generator;

    AbstractActor(Behaviour<T> behaviour) {
        this.mailbox = new MailBox<>();
        this.address = UUID.randomUUID().toString();
        this.behaviour = behaviour;
        this.messageHandler = this.handleMessages();
        this.parentAddress = "";
        this.isTerminated = false;
        this.children = new ArrayList<>();
    }


    public final boolean tell(T message) {
        return this.mailbox.send(message);
    }

    public final String toString() {
        return this.address;
    }

    public final <E extends Message> ActorRef<E> spawn(Function<Behaviour<E>, AbstractActor<E>> generator, String childAddress) {
        AbstractActor<E> child = Actors.newAbstractActor(generator);
        child.setParentAddress(this.address); //Since the parent is recreating the child, we're using this.address
        child.setAddress(childAddress);
        getContext().registerActor(child);
        this.children.add(new ActorMetadata(childAddress, child, child.generator));
        return child;
    }

    public final <E extends Message> ActorRef<E> spawn(Function<Behaviour<E>, AbstractActor<E>> generator) {
        AbstractActor<E> child = Actors.newAbstractActor(generator);
        child.setParentAddress(this.address); //Since the parent is recreating the child, we're using this.address
        getContext().registerActor(child);
        this.children.add(new ActorMetadata(child.toString(), child, child.generator));
        return child;
    }

    public void start() {
        ActorSystem.executor()
                .execute(() -> {
                    this.preStart();
                    Thread.currentThread().setName(this.address);
                    try {
                        while (!this.isTerminated) {
                            final Optional<T> opt = this.mailbox.receive();
                            Behaviour<T> nextBehaviour;
                            if (opt.isEmpty()) return;

                            T val = opt.get();
                            if (this.behaviour instanceof Behaviour.Sink<T>)
                                continue; //If the behaviour is already a sink fk it

                            if (val instanceof ChildDeath(var childAddress, var gen, var list)) {
                                this.handleChildDeath(childAddress, gen, list);
                                continue;
                            }

                            nextBehaviour = this.messageHandler.get(val);
                            if (nextBehaviour != null)
                                nextBehaviour = nextBehaviour.change(val); //Fetch the behaviour bound to this message type
                            if (!(this.behaviour instanceof Behaviour.Same<T>)) this.behaviour = nextBehaviour;

                        }
                    } catch (Exception e) {
                        this.onException(e);
                    }
                });
    }

    private void onException(Exception e) {
        this.stop();
        this.children.stream()
                .map(am -> am.lifeCycle)
                .forEach(ActorLifeCycle::stop);
        if (!this.parentAddress.isBlank()) {
            getContext().send(this.parentAddress, new ChildDeath(this.address, this.generator, List.copyOf(this.children)));
        }

        throw new ChildDeathException(e); //Kill the thread
    }

    @SuppressWarnings("unchecked")
    private void handleChildDeath(String childAddress, RawActorGenerator gen, List<ActorMetadata> list) {
        ActorRef<?> parent = this.spawn(
                (behaviour) -> (AbstractActor<Message>) gen.create(behaviour),
                childAddress
        );

        list.forEach(am -> parent.spawn(
                (b) -> (AbstractActor<Message>) gen.create(b),
                am.address));

        this.onChildRestart();
    }


    void setParentAddress(String address) {
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

    public void stop() {
        this.preStop();
        getContext().remove(this.address);
        this.behaviour = Behaviour.sink();
        this.isTerminated = true;
        this.children.stream().map(am -> am.lifeCycle).forEach(ActorLifeCycle::stop);
        run(this.mailbox::close);
    }

    public String getParent() {
        return this.parentAddress;
    }

    //These are to be overridden. Didn't make the lifecycle hooks abstract, just for QOL
    public void preStop() {
    }

    public void preStart() {
    }

    public void onChildRestart() {
    }

    public abstract MessageHandler<T> handleMessages();

    record ActorMetadata(String address, ActorLifeCycle lifeCycle, RawActorGenerator generator) {
    }

    record ChildDeath(String address, RawActorGenerator generator, List<ActorMetadata> children) implements Message {
    }
}