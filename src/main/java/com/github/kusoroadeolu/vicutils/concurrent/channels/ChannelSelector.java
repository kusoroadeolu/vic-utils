package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class ChannelSelector<T>{
    private final ReceiveChannel<T>[] channels;
    private final Map<ReceiveChannel<T>, Consumer<T>> map;
    private final static ExecutorService EXEC = Executors.newVirtualThreadPerTaskExecutor();
    private T fallback;
    private long timeout;
    private final Lock lock;
    private final Condition condition;


     ChannelSelector(ReceiveChannel<T>[] channels) {
        this.channels = channels;
        this.map = new HashMap<>();
        Arrays.stream(this.channels).forEach(c -> this.map.put(c, null));
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

    public T execute(){
        final var selectorList = new SelectorList<T>();
        this.lock.lock();
        try {
            for (ReceiveChannel<T> c: this.channels){
                CompletableFuture.runAsync(() -> {
                    final Optional<T> val = c.receive();
                    T t = val.orElse(this.fallback);
                    selectorList.add(c, t , this.map, this.lock, this.condition);
                }, EXEC);
            }

            if (this.timeout != -1){
                boolean timeNotUp = true;
                while (selectorList.isEmpty() && timeNotUp){
                    timeNotUp = this.condition.await(timeout, TimeUnit.MILLISECONDS);
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

        this.throwIfNotEmpty(selectorList);
        return selectorList.getFirst(this.fallback);
    }

    private void throwIfNotEmpty(SelectorList<T> list){
        if (!list.throwableList.isEmpty()) throw new RuntimeException(list.throwableList.getFirst());
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

        public void add(ReceiveChannel<T> chan, T val, Map<ReceiveChannel<T>, Consumer<T>> map, Lock bl, Condition condition){
            this.lock.lock();
            try {
                if (!this.list.isEmpty()) return;
                this.list.add(val);
            }finally {
                this.lock.unlock();
            }

            var v = map.get(chan);
            if (v != null) {
                try {
                    v.accept(val);
                } catch (Exception e) {
                    this.throwableList.add(e);
                }
            }

            bl.lock();
            try {
                condition.signalAll();
            }finally {
                bl.unlock();
            }
        }

        public T getFirst(T fallback){
            try {
                 return this.list.getFirst();
            }catch (Exception e){
                return fallback;
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

}
