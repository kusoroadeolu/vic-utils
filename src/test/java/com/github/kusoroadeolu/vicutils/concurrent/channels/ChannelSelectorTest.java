package com.github.kusoroadeolu.vicutils.concurrent.channels;

import org.junit.jupiter.api.Test;

import java.lang.invoke.VarHandle;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ChannelSelectorTest {

    @Test
    public void shouldReturnAValue_onChannelSelectReceive(){
        Channel<Integer> chan1 = new UnBufferedChannel<>();
        Channel<Integer> chan2 = new UnBufferedChannel<>();
        Channel<Integer> chan3 = new UnBufferedChannel<>();
        chan1.make(); chan2.make(); chan3.make();
        chan1.trySend(1); chan2.trySend(2); chan3.trySend(3);

       Integer val = ChannelSelector.selectReceive(chan1, chan2, chan3).receive();
       assertNotNull(val);
    }

    @Test
    public void shouldTimeout_afterGivenTimeout() {
        Channel<Integer> chan1 = new UnBufferedChannel<>();
        chan1.make();
        Integer val = ChannelSelector.selectReceive(chan1).timeout(1000).receive();
        assertNull(val);
    }

    @Test
    public void shouldReturnDefaultVal_onEmptyOptional(){
        Channel<Integer> chan1 = new UnBufferedChannel<>();
        chan1.make();
        Integer val = ChannelSelector.selectReceive(chan1).timeout(1000).defaultTo(10).receive();
        assertEquals(10, val);
    }

    @Test
    void stressTest_raceCondition_onlyOneWins() throws InterruptedException {
        Channel<Integer> chan1 = new UnBufferedChannel<>();
        Channel<Integer> chan2 = new UnBufferedChannel<>();
        Channel<Integer> chan3 = new UnBufferedChannel<>();
        chan1.make(); chan2.make(); chan3.make();

        AtomicInteger consumerCount = new AtomicInteger(0);
        Consumer<Integer> counter = _ -> consumerCount.incrementAndGet();

        Thread.startVirtualThread(() -> chan1.send(1));
        Thread.startVirtualThread(() -> chan2.send(2));
        Thread.startVirtualThread(() -> chan3.send(3));

        Thread.sleep(50);

        Integer val = ChannelSelector.selectReceive(chan1, chan2, chan3)
                .onReceive(chan1, counter)
                .onReceive(chan2, counter)
                .onReceive(chan3, counter)
                .receive();

        assertNotNull(val);
        assertEquals(1, consumerCount.get());
    }

    @Test
    void stressTest_threadLeakCheck() {
        Channel<Integer> chan1 = new UnBufferedChannel<>();
        chan1.make();

        for (int i = 0; i < 1000; i++) {
            Channel<Integer> tempChan = new UnBufferedChannel<>();
            tempChan.make();


            ChannelSelector.selectReceive(chan1, tempChan)
                    .timeout(10)
                    .receive();
        }

    }

    @Test
    void stressTest_manyChannels() {
        @SuppressWarnings("unchecked")
        Channel<Integer>[] channels = new Channel[100];
        for (int i = 0; i < 100; i++) {
            channels[i] = new UnBufferedChannel<>();
            channels[i].make();
        }

        int winner = ThreadLocalRandom.current().nextInt(100);
        channels[winner].trySend(42);

        Integer val = ChannelSelector.selectReceive(channels).receive();
        assertEquals(42, val);
    }

    @Test
    void stressTest_throwingConsumer() {
        Channel<Integer> chan = new UnBufferedChannel<>();
        chan.make();
        chan.trySend(1);
        assertFalse(chan.isEmpty());
        assertThrows(RuntimeException.class, () -> {
            ChannelSelector.selectReceive(chan)
                    .onReceive(chan, v -> {throw new RuntimeException("Oops!");})
                    .receive();
        });
    }



}