package com.github.kusoroadeolu.vicutils.concurrent.channels;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnBufferedChannelTest{

    ExecutorService vExec;

    @BeforeEach
    void setup(){
        vExec = Executors.newVirtualThreadPerTaskExecutor();
    }

     @Test
     void onMake_shouldOpenChannel(){
        Channel<?> chan = new UnBufferedChannel<>();
        assertFalse(chan.ok());
        chan.make();
        assertTrue(chan.ok());
     }

     @Test
     void onSend_shouldWaitIfNil(){
         Channel<Integer> chan = new UnBufferedChannel<>();
         Thread t = Thread.startVirtualThread(() -> chan.send(1));
         assertTrue(chan.isEmpty());
     }

     @Test
     void onClose_shouldThrowIfNil(){
         Channel<?> chan = new UnBufferedChannel<>();
         assertThrows(ChannelNilException.class, chan::close);
     }

     @Test
     void onSend_shouldReceiveTheSentValue() throws ExecutionException, InterruptedException {
         Channel<Integer> chan = new UnBufferedChannel<>();
         SendChannel<Integer> sc = chan.makeSendChannel();
         ReceiveChannel<Integer> rc = chan.makeReceiveChannel();

         CompletableFuture<Void> cs = CompletableFuture.runAsync(() -> sc.send(1));
         CompletableFuture<Optional<Integer>> cr = CompletableFuture.supplyAsync(rc::receive);
         CompletableFuture.allOf(cr, cs).join();

         assertEquals(1, cr.get().get());
     }


     @Test
     void unBufChannel_onMultiSend_shouldWaitForReceivers(){
         Channel<Integer> chan = new UnBufferedChannel<>();
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
     }

     @Test
     void onClose_shouldThrow_onSends(){
         Channel<Integer> chan = new UnBufferedChannel<>();
         chan.make();
         chan.close();
         assertThrows(ChannelClosedException.class, () -> chan.send(1));
     }

     @Test
     void onClose_receiversShouldBeAllowedDrainChannel(){
         Channel<Integer> chan = new BufferedChannel<>(100);
         chan.make();

         List<CompletableFuture<Void>> futures = new ArrayList<>();

         for (int i = 0; i < 100; i++){
             SendChannel<Integer> sc = chan.makeSendChannel();
             var v = CompletableFuture.runAsync(() -> sc.send(1), vExec);
             futures.add(v);
         }

         CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
         chan.close();
         List<Integer> list = new ArrayList<>();
         Optional<Integer> opt;

         while((opt = chan.receive()).isPresent()){
             list.add(opt.get());
         }

         assertTrue(chan.isEmpty());
         assertEquals(100, list.size());
     }

     @Test
     void onCloseAndChannelEmpty_shouldReturnEmpty_ifReceive(){
         Channel<Integer> chan = new UnBufferedChannel<>();
         chan.make();
         chan.close();
         Optional<Integer> opt = chan.receive();
         assertTrue(opt.isEmpty());
     }

    @Test
    void onClose_ifClosed_shouldThrowEx(){
        Channel<Integer> chan = new UnBufferedChannel<>();
        chan.make();
        chan.close();
        assertThrows(ChannelClosedException.class, chan::close);
    }

    @Test
    void onClose_shouldSuccessfullyClose_a_madeChannel(){
        Channel<Integer> chan = new UnBufferedChannel<>();
        chan.make();
        assertDoesNotThrow(chan::close);
    }

    @Test
    void onClose_assertReceiveCompletes_regardlessOfOrder() throws ExecutionException, InterruptedException {
        Channel<Integer> chan = new UnBufferedChannel<>();
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
        Channel<Integer> chan = new UnBufferedChannel<>();
        chan.make();
        chan.trySend(1);
        assertFalse(chan.isEmpty());
    }

    @Test
    void onTryReceive_shouldNotBlock(){
        Channel<Integer> chan = new UnBufferedChannel<>();
        chan.make();
        Optional<Integer> val = chan.tryReceive();
        assertTrue(val.isEmpty());
    }

    @Test
    void stressTest_massiveConcurrentSendReceive() {
        Channel<Integer> chan = new UnBufferedChannel<>();
        chan.make();

        int numPairs = 10000;
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numPairs; i++) {
            final int val = i;
            futures.add(CompletableFuture.runAsync(() -> chan.send(val), vExec));
            futures.add(CompletableFuture.runAsync(chan::receive, vExec));
        }

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        assertTrue(chan.isEmpty());
    }

    @Test
    void stressTest_closeUnderLoad() throws InterruptedException {
        Channel<Integer> chan = new UnBufferedChannel<>();
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
    void stressTest_interruptedThreads() {
        Channel<Integer> chan = new UnBufferedChannel<>();
        chan.make();

        Thread sender = Thread.startVirtualThread(() -> chan.send(1));
        sender.interrupt();
        assertTrue(chan.isEmpty());
    }

    @Test
    void pingPong_aValue_onTwoThreads() throws InterruptedException {
        Channel<Integer> chan = new UnBufferedChannel<>();
        ReceiveChannel<Integer> rc1 = chan.makeReceiveChannel();
        SendChannel<Integer> sc1 = chan.makeSendChannel();

        Channel<Integer> chan2 = new UnBufferedChannel<>();
        ReceiveChannel<Integer> rc2 = chan2.makeReceiveChannel();
        SendChannel<Integer> sc2 = chan2.makeSendChannel();
        Await await = new Await();

        Thread.startVirtualThread(() -> {
            var val = 0;
            while (!await.stop){
                sc1.send(val);
                val = rc2.receive().get();
                IO.println("Val: " + val);

            }
        });

        Thread.startVirtualThread(() -> {
            while (!await.stop){
                var val = rc1.receive().get();
                sc2.send(val);
                IO.println("Val: " + val);

            }
        });

        Thread.sleep(6000);
        await.setStop(true);
    }

    @Test
    void onNilChannel_testMakeUnderConcurrency(){
        Channel<Integer> chan = new UnBufferedChannel<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 100; i++){
            futures.add(CompletableFuture.runAsync(chan::make, vExec));
        }

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        assertTrue(chan.ok());
        
    }


}

class Await{
    volatile boolean stop = false;

    public Await setStop(boolean stop) {
        this.stop = stop;
        return this;
    }
}