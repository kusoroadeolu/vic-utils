package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
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
    private final Lock lock;
    private final Condition condition;


     ChannelSelector(ReceiveChannel<T>[] channels) {
        this.channels = channels;
        this.map = new HashMap<>();
        this.fallback = null;
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
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

    public ChannelSelector<T> timeout(long timeout) {
        if (timeout < 0) throw new IllegalArgumentException();
        this.timeout = timeout;
        return this;
    }

    public T execute(){
        final var selectorList = new SelectorList<T>();
        final var futures = new ArrayList<CompletableFuture<Void>>();
        final var await = new Await();

        this.lock.lock();
        try {
            for (ReceiveChannel<T> c: channels){
                futures.add(CompletableFuture.runAsync(() -> {
                    final var val = c.receive();
                    selectorList.add(c, val.orElse(this.fallback), this.map, this.lock, this.condition);
                }, EXECUTOR_SERVICE));
            }

            if (this.timeout != -1){
                SCHEDULED_EXECUTOR.schedule(() -> {
                    await.setTimeUp(true);
                    this.lock.lock();
                    try {
                        condition.signal();
                    }finally {
                        this.lock.unlock();
                    }

                }, this.timeout, TimeUnit.MILLISECONDS);
                while (selectorList.isEmpty() && !await.timeUp){
                    IO.println("Is list empty: " + selectorList.isEmpty());
                    this.condition.await();
                }

            }else{
                while(selectorList.isEmpty()){
                    this.condition.await();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            this.lock.unlock();
        }


        return selectorList.getFirst();
    }


    private static class SelectorList<T>{
        private final List<T> list;
        private final static int MAX_SIZE = 1;
        private final Lock lock;

        SelectorList() {
            this.list = new ArrayList<>(MAX_SIZE);
            this.lock = new ReentrantLock();
        }

        public void add(ReceiveChannel<T> chan, T val, Map<ReceiveChannel<T>, Consumer<T>> map, Lock bl, Condition condition){
            this.lock.lock();
            try {
                if (this.list.isEmpty()) {
                    this.list.add(val);
                    var v = map.get(chan);
                    if (v != null) v.accept(val);
                    bl.lock();
                    try {
                        condition.signal();
                    }finally {
                        bl.unlock();
                    }

                }

            }finally {
                this.lock.unlock();
            }
        }

        public T getFirst(){
            try {
                return this.list.getFirst();
            }catch (Exception e){
                return null;
            }
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
