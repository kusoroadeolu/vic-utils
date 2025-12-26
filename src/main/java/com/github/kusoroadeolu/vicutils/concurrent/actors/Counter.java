package com.github.kusoroadeolu.vicutils.concurrent.actors;

public class Counter extends AbstractActor<Message>{
    private int counter = 0;

    public Counter(Behaviour<Message> behaviour) {
        super(behaviour);
    }

    @Override
    public MessageHandler<Message> handleMessages() {
        return MessageHandler.<Message>builder()
                .onMessage(Increment.class, onIncrementMessage())
                .onMessage(Decrement.class, onDecrementMessage())
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

    public Behaviour<Message> onDecrementMessage(){
        return msg -> {
            Decrement decrement = (Decrement) msg;
            counter -= decrement.decrBy;
            IO.println(counter);
            return Behaviour.same();
        };
    }

    public int getCounter(){
        return this.counter;
    }

    public record Increment(int incrBy) implements Message{

    }

    public record Decrement(int decrBy) implements Message{

    }
}
