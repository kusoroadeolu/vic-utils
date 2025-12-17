package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChannelSelector<T> {
    private final ExecutorService executorService;
    public ChannelSelector() {
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    public T select(ReceiveChannel<T>... channels){
        final var selectorList = new SelectorList<T>();
        final var futures = new ArrayList<CompletableFuture<Void>>();
        for (ReceiveChannel<T> c: channels){
            futures.add(CompletableFuture.runAsync(() -> {
                final var val = c.receive();
                selectorList.add(val);
            }, this.executorService));
        }

        while (selectorList.isEmpty()){
            Thread.onSpinWait();
        }

        return selectorList.list.getFirst();
    }


    private static class SelectorList<T>{
        private final List<T> list;
        private final static int MAX_SIZE = 1;
        private final Lock lock;
        SelectorList() {
            this.list = new ArrayList<>(MAX_SIZE);
            this.lock = new ReentrantLock();
        }

        public void add(T val){
            this.lock.lock();
            try {
                if (this.list.isEmpty()) this.list.add(val);
            }finally {
                this.lock.unlock();
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
