package com.github.kusoroadeolu.vicutils.concurrent.actors;


// A thread safe counter using actor model
public class Counter extends AbstractActor<Message>{
    private volatile int counter = 0;

    public Counter(Behaviour<Message> behaviour) {
        super(behaviour);
    }

    @Override
    public MessageHandler<Message> handleMessages() {
        return MessageHandler.<Message>builder()
                .onMessage(Increment.class, onIncrementMessage())
                .onMessage(Decrement.class, onDecrementMessage())
                .onMessage(ExMessage.class, onExceptionMessage())
                .build();
    }


    public Behaviour<Message> onIncrementMessage(){
        return msg -> {
            Increment increment = (Increment) msg;
            counter += increment.incrBy();
            IO.println(counter);
            return Behaviour.same();
        };
    }

    public Behaviour<Message> onExceptionMessage(){
        return msg -> {
            ExMessage exMessage = (ExMessage) msg;
            throw exMessage.e();
        };
    }

    public Behaviour<Message> onDecrementMessage(){
        return msg -> {
            Decrement decrement = (Decrement) msg;
            counter -= decrement.decrBy;
            IO.println(counter);
            return Behaviour.same();
        };
    }

    //Just for tests nothing more
    public int getCounter(){
        return this.counter;
    }

    public record Increment(int incrBy) implements Message{

    }

    public record Decrement(int decrBy) implements Message{

    }

    public record ExMessage(RuntimeException e) implements Message{}
}
