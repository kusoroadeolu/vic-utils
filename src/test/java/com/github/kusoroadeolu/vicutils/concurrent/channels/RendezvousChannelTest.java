package com.github.kusoroadeolu.vicutils.concurrent.channels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


//A test similar to unbuffered channel's test
class RendezvousChannelTest {


    ExecutorService vExec;

    @BeforeEach
    void setup(){
        vExec = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Test
    void onMake_shouldOpenChannel(){
        Channel<?> chan = new RendezvousChannel<>();
        assertFalse(chan.ok());
        chan.make();
        assertTrue(chan.ok());
    }

    @Test
    void onSend_shouldThrowIfNil(){
        Channel<Integer> chan = new RendezvousChannel<>();
        assertThrows(ChannelNilException.class, () -> chan.send(1));
        assertTrue(chan.isEmpty());
    }

    @Test
    void onClose_shouldThrowIfNil(){
        Channel<?> chan = new RendezvousChannel<>();
        assertThrows(ChannelNilException.class, chan::close);
    }

    @Test
    void onSend_shouldReceiveTheSentValue() throws ExecutionException, InterruptedException {
        Channel<Integer> chan = new RendezvousChannel<>();
        SendChannel<Integer> sc = chan.makeSendChannel();
        ReceiveChannel<Integer> rc = chan.makeReceiveChannel();

        CompletableFuture<Void> cs = CompletableFuture.runAsync(() -> sc.send(1));
        CompletableFuture<Optional<Integer>> cr = CompletableFuture.supplyAsync(rc::receive);
        CompletableFuture.allOf(cr, cs).join();

        assertEquals(1, cr.get().get());
    }


    @Test
    void unBufChannel_onMultiSend_shouldWaitForReceivers(){
        Channel<Integer> chan = new RendezvousChannel<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 100; i++){
            SendChannel<Integer> sc = chan.makeSendChannel();
            var v = CompletableFuture.runAsync(() -> sc.send(1), vExec);
            futures.add(v);
        }


        ReceiveChannel<Integer> rc = chan.makeReceiveChannel();
        Optional<Integer> opt = rc.receive();
        assertEquals(1, opt.get());
        assertFalse(chan.isEmpty());
        ArrayBlockingQueue
    }

    @Test
    void onClose_shouldThrow_onSends(){
        Channel<Integer> chan = new RendezvousChannel<>();
        chan.make();
        chan.close();
        assertThrows(ChannelClosedException.class, () -> chan.send(1));
    }

    @Test
    void onCloseAndChannelEmpty_shouldReturnEmpty_ifReceive(){
        Channel<Integer> chan = new RendezvousChannel<>();
        chan.make();
        chan.close();
        Optional<Integer> opt = chan.receive();
        assertTrue(opt.isEmpty());
    }

    @Test
    void onClose_ifClosed_shouldThrowEx(){
        Channel<Integer> chan = new RendezvousChannel<>();
        chan.make();
        chan.close();
        assertThrows(ChannelClosedException.class, chan::close);
    }

    @Test
    void onClose_shouldSuccessfullyClose_a_madeChannel(){
        Channel<Integer> chan = new RendezvousChannel<>();
        chan.make();
        assertDoesNotThrow(chan::close);
    }

    @Test
    void onClose_assertReceiveCompletes_regardlessOfOrder() throws ExecutionException, InterruptedException {
        Channel<Integer> chan = new RendezvousChannel<>();
        chan.make();
        Thread.startVirtualThread(() -> chan.send(1));
        var v = CompletableFuture.supplyAsync(chan::receive, vExec);
        var w = CompletableFuture.runAsync(chan::close, vExec);
        v.join(); w.join();
        assertNotNull(v.get().get());
        assertTrue(chan.isEmpty());
    }

    @Test
    void onTrySend_shouldNotBlock(){
        Channel<Integer> chan = new RendezvousChannel<>();
        chan.make();
        chan.trySend(1);
        assertFalse(chan.isEmpty());
    }

    @Test
    void onTryReceive_shouldNotBlock(){
        Channel<Integer> chan = new RendezvousChannel<>();
        chan.make();
        Optional<Integer> val = chan.tryReceive();
        assertTrue(val.isEmpty());
    }

    @Test
    void stressTest_closeUnderLoad() throws InterruptedException {
        Channel<Integer> chan = new RendezvousChannel<>();
        chan.make();
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            vExec.submit(() -> {
                try {
                    chan.send(1);
                    latch.countDown();
                } catch (ChannelClosedException ignored) {
                    latch.countDown();
                }
            });
        }

        chan.close();
        latch.await();
        assertEquals(0, latch.getCount());

    }


    @Test
    void onNilChannel_testMakeUnderConcurrency(){
        Channel<Integer> chan = new RendezvousChannel<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 100; i++){
            futures.add(CompletableFuture.runAsync(chan::make, vExec));
        }

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        assertTrue(chan.ok());

    }


    @Test
    void stressTest_massiveConcurrentSendReceive() {
        Executor vExec = Executors.newVirtualThreadPerTaskExecutor();
        RendezvousChannel<Integer> chan = new RendezvousChannel<>();
        List<Integer> ls = new CopyOnWriteArrayList<>();
        chan.make();

        int numPairs = 100;
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numPairs; i++) {
            final int val = i;
            futures.add(CompletableFuture.runAsync(() -> chan.send(val), vExec));
            futures.add(CompletableFuture.runAsync(() -> {
                Optional<Integer> opt = chan.receive();
                ls.add(opt.get());
            }, vExec));
        }



        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        assertEquals(100, new HashSet<>(ls).size()); // No duplicates
        assertTrue(ls.containsAll(IntStream.range(0, 100).boxed().toList()));
        assertTrue(chan.isEmpty());
    }
}