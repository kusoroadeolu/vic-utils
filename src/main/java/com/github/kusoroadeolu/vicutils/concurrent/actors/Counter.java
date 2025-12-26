package com.github.kusoroadeolu.vicutils.concurrent.actors;

public class Counter extends AbstractActor<String>{
    private int counter = 0;

    public Counter(Behaviour<String> behaviour) {
        super(behaviour);
    }

    @Override
    public MessageHandler<String> handleMessages() {
        return MessageHandler.<String>builder()
                .onMessageEquals("inc", onIncrementMessage())
                .onMessageEquals("dec", onDecrementMessage())
                .build();
    }


    public Behaviour<String> onIncrementMessage(){
        return message -> {
            ++counter;
            IO.println(counter);
            return Behaviour.sink();
        };
    }

    public Behaviour<String> onDecrementMessage(){
        return message -> {
            --counter;
            IO.println(counter);
            return Behaviour.sink();
        };
    }

    public int getCounter(){
        return this.counter;
    }

    private interface Increment extends Message{

    }

    private interface Decrement extends Message{

    }
}
