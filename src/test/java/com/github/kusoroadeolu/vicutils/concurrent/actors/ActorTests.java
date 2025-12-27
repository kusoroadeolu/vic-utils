package com.github.kusoroadeolu.vicutils.concurrent.actors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActorTests {

    private ActorSystem ctx;

    @BeforeEach
    public void setup(){
        ctx = ActorSystem.getContext();
    }

    @Test
    public void onChildRestart_shouldRetainParentAddress() throws InterruptedException {
        String childAddress = "child", parentAddress = "parent";
        ActorRef<Message> parent = ctx.createActor(TestActor::new, parentAddress);
        ActorRef<Message> child = parent.spawn(TestActor::new, childAddress);
        child.tell(new TestActor.ExceptionMessage());
        Thread.sleep(100); //Wait for a while for the parent to process the child crash
        ActorRef<Message> restartedChild = ctx.getActor(childAddress);
        assertNotNull(restartedChild);
        assertEquals(childAddress, restartedChild.toString());
    }

    @Test
    public void onDeath_systemShouldRestartParent() throws InterruptedException {
        String parentAddress = "parent";
        ActorRef<Message> parent = ctx.createActor(TestActor::new, parentAddress);
        parent.tell(new TestActor.ExceptionMessage());
        Thread.sleep(100);
        ActorRef<Message> restartedParent = ctx.getActor(parentAddress);
        assertNotNull(restartedParent);
    }

    @Test
    public void onParentDeath_whileChildDies_shouldRestartChild() throws InterruptedException {
        String parentAddress = "parent", childAddress = "child";
        ActorRef<Message> parent = ctx.createActor(TestActor::new, parentAddress);
        ActorRef<Message> child = parent.spawn(TestActor::new, childAddress);
        try {
            parent.tell(new TestActor.ExceptionMessage());
            child.tell(new TestActor.ExceptionMessage());
        }catch (Exception ignored){

        }

        Thread.sleep(1000);
        ActorRef<Message> restartedParent = ctx.getActor(parentAddress);
        ActorRef<Message> restartedChild = ctx.getActor(childAddress);
        assertNotNull(restartedParent);
        assertNotNull(restartedChild);
    }


    @Test
    public void onClose_shouldClearSystemCtx(){
        for (int i = 0; i < 100; i++){
            ctx.createActor(TestActor::new);
        }

        assertEquals(101, ctx.size()); //100 including the system actor
        ctx.close();
        assertEquals(0, ctx.size());
    }
}

class TestActor extends AbstractActor<Message>{
    public TestActor(Behaviour<Message> behaviour) {
        super(behaviour);
    }

    @Override
    public MessageHandler<Message> handleMessages() {
        return MessageHandler.<Message>builder()
                .onMessage(ExceptionMessage.class, exBehavior())
                .build();
    }

    public Behaviour<Message> exBehavior(){
        return _ -> {
            throw new RuntimeException();
        };
    }

    record ExceptionMessage() implements Message{}
}