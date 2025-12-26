package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class ChannelSelector<T>{
    private final ReceiveChannel<T>[] channels;
    private final Map<ReceiveChannel<T>, Consumer<T>> map;
    private final AtomicBoolean executed;
    private final static ExecutorService EXEC = Executors.newVirtualThreadPerTaskExecutor();
    private final static String MESSAGE = "Selector can only be executed once";
    private T fallback;
    private long timeout;

     ChannelSelector(ReceiveChannel<T>[] channels) {
        this.channels = channels;
        this.map = new HashMap<>();
        Arrays.stream(this.channels).forEach(c -> this.map.put(c, null));
        this.fallback = null;
        this.timeout = -1;
        this.executed = new AtomicBoolean(false);
    }


    @SafeVarargs
    public static <T> ChannelSelector<T> selectReceive(ReceiveChannel<T>... channels){
        requireNonNull(channels);
        return new ChannelSelector<>(channels);
    }

    public ChannelSelector<T> onReceive(ReceiveChannel<T> channel, Consumer<T> result){
        requireNonNull(channel);
        if (this.map.containsKey(channel)) this.map.put(channel, result);
        return this;
    }

    public ChannelSelector<T> defaultTo(T val){
        requireNonNull(val);
        this.fallback = val;
        return this;
    }

    public ChannelSelector<T> timeout(long millis) {
        if (millis < 0) throw new IllegalArgumentException();
        this.timeout = millis;
        return this;
    }

    public T receive(){
        if (!this.executed.compareAndSet(false, true)) throw new SelectionError(MESSAGE);
        final var latch = new CountDownLatch(1);
        final var selectorList = new SelectorList<T>();
        final var futures = new ArrayList<CompletableFuture<?>>();
        try {
            for (ReceiveChannel<T> c: this.channels){
                var cf = CompletableFuture.runAsync(() -> {
                    final Optional<T> val = c.receive();
                    T t = val.orElse(this.fallback);
                    selectorList.add(c, t , this.map, latch);
                }, EXEC);
                futures.add(cf);
            }

            if(this.timeout != -1) latch.await(this.timeout, TimeUnit.MILLISECONDS);
            else latch.await();

            this.cancelFutures(futures);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        this.throwIfNotEmpty(selectorList);
        return selectorList.getFirst(this.fallback);
    }


    private void cancelFutures(List<CompletableFuture<?>> futures) {
         CompletableFuture.runAsync(() -> {
             for (CompletableFuture<?> c : futures){
                 c.cancel(true);
             }
         }, EXEC);
    }

    private void throwIfNotEmpty(SelectorList<T> list){
        if (!list.throwableListEmpty()) throw new RuntimeException(list.throwableGetFirst());
    }


    private static class SelectorList<T>{
        private final List<T> list;
        private final List<Throwable> throwableList;
        private final static int MAX_SIZE = 1;
        private final Lock lock;

        SelectorList() {
            this.list = new ArrayList<>(MAX_SIZE);
            this.throwableList = new ArrayList<>(MAX_SIZE);
            this.lock = new ReentrantLock();
        }

        public void add(ReceiveChannel<T> chan, T val, Map<ReceiveChannel<T>, Consumer<T>> map, CountDownLatch latch){
            this.lock.lock();
            try {
                if (!this.list.isEmpty()) return;
                this.list.add(val);
            }finally {
                this.lock.unlock();
            }

            final var v = map.get(chan);
            if (v != null) {
                try {
                    v.accept(val);
                } catch (Exception e) {
                    this.throwableList.add(e);
                }
            }

            latch.countDown();
        }


        public T getFirst(T fallback){
            try {
                return this.list.getFirst();
            }catch (Exception e){
                return fallback;
            }
        }

        public boolean throwableListEmpty(){
            return this.throwableList.isEmpty();
        }

        public Throwable throwableGetFirst(){
            return this.throwableList.getFirst();
        }

    }

}
