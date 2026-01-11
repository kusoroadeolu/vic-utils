package com.github.kusoroadeolu.vicutils.ds;


import com.github.kusoroadeolu.vicutils.concurrent.locked.Locked;

import java.util.concurrent.locks.ReentrantLock;

// A LRU Cache with one global lock
public class SynchronizedLRUCache<K, V> implements LRUCache<K, V>{

    private final Locked<SequentialLRUCache<K, V>> lockedCache;
    private final int capacity;

    public SynchronizedLRUCache() {
        SequentialLRUCache<K,V> cache = new SequentialLRUCache<>();
        this.lockedCache = new Locked<>(cache);
        this.capacity = cache.capacity();
    }

    public SynchronizedLRUCache(int capacity) {
        this.lockedCache = new Locked<>(new SequentialLRUCache<>(capacity));
        this.capacity = capacity;
    }

    @Override
    public V put(K k, V v) {
        return this.lockedCache.supply(c -> c.put(k, v));
    }

    @Override
    public V get(K k) {
        return this.lockedCache.supply(c -> c.get(k));
    }

    @Override
    public V evict(K k) {
        return this.lockedCache.supply(c -> c.evict(k));
    }

    @Override
    public V getMostRecentlyUsed() {
        return this.lockedCache.supply(SequentialLRUCache::getMostRecentlyUsed);
    }

    @Override
    public V getLeastRecentlyUsed() {
        return this.lockedCache.supply(SequentialLRUCache::getLeastRecentlyUsed);
    }

    @Override
    public int size() {
        return this.lockedCache.supply(SequentialLRUCache::size);
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public V evictLeastRecentlyUsed() {
        return this.lockedCache.supply(SequentialLRUCache::evictLeastRecentlyUsed);
    }

    @Override
    public V evictMostRecentlyUsed() {
        return this.lockedCache.supply(SequentialLRUCache::evictMostRecentlyUsed);
    }
}
