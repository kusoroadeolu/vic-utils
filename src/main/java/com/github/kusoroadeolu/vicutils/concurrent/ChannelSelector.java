package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class ChannelSelector<T>{
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newVirtualThreadPerTaskExecutor();
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(1000, Thread.ofVirtual().factory());
    private final ReceiveChannel<T>[] channels;
    private final Map<ReceiveChannel<T>, Consumer<T>> map;
    private T fallback;
    private long timeout;


     ChannelSelector(ReceiveChannel<T>[] channels) {
        this.channels = channels;
        this.map = new HashMap<>();
        this.fallback = null;
        this.timeout = -1;
    }

    @SafeVarargs
    public static <T>ChannelSelector<T> select(ReceiveChannel<T>... channels){
         requireNonNull(channels);
         return new ChannelSelector<>(channels);
    }

    public ChannelSelector<T> onReceive(ReceiveChannel<T> channel, Consumer<T> result){
        requireNonNull(channel);
        this.map.put(channel, result);
        return this;
    }

    public ChannelSelector<T> defaultTo(T val){
        requireNonNull(val);
        this.fallback = val;
        return this;
    }

    public ChannelSelector<T> setTimeout(long timeout) {
        if (timeout < 0) throw new IllegalArgumentException();
        this.timeout = timeout;
        return this;
    }

    public T execute(){
        final var selectorList = new SelectorList<Optional<T>();
        final var futures = new ArrayList<CompletableFuture<Void>>();
        final var await = new Await();
        for (ReceiveChannel<T> c: channels){
            futures.add(CompletableFuture.runAsync(() -> {
                final var val = c.receive();
                selectorList.add(c, val, map);
            }, EXECUTOR_SERVICE));
        }

        if (this.timeout != -1){
            SCHEDULED_EXECUTOR.schedule(() -> await.setTimeUp(true), timeout, TimeUnit.MILLISECONDS);
            while (selectorList.isEmpty() && !await.timeUp){
                Thread.onSpinWait();
            }
        }else{
            while (selectorList.isEmpty()){
                Thread.onSpinWait();
            }
        }



        return selectorList.list.getFirst().orElse(this.fallback);
    }


    private static class SelectorList<T>{
        private final List<T> list;
        private final static int MAX_SIZE = 1;
        private final Lock lock;

        SelectorList() {
            this.list = new ArrayList<>(MAX_SIZE);
            this.lock = new ReentrantLock();
        }

        public void add(ReceiveChannel<Optional<T>> chan, T val, Map<ReceiveChannel<Optional<T>>, Consumer<T>> map){
            this.lock.lock();
            try {
                if (this.list.isEmpty()) {
                    this.list.add(val);
                    var v = map.get(chan);
                    if (v != null) v.accept(val);
                }

            }finally {
                this.lock.unlock();
            }
        }

        public T getFirst(){
            return this.list.getFirst();
        }



        public boolean isEmpty(){
            this.lock.lock();
            try {
                return this.list.isEmpty();
            }finally {
                this.lock.unlock();
            }
        }
    }

    public static class Await{
        private volatile boolean timeUp = false;

        public void setTimeUp(boolean timeUp) {
            this.timeUp = timeUp;
        }
    }
}
