package com.github.kusoroadeolu.vicutils.ds;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.unmodifiableMap;

public class ConcurrentLRUCache<K, V> implements SegmentedLRUCache<K, V>{
    private static final int SEGMENT_COUNT= 1 << 4;
    private final Map<Integer, Segment<K, V>> segments;
    private final int capacity;
    final AtomicInteger atomicSize;
    //The capacity per segment
    public ConcurrentLRUCache(int capacity) {
        this.segments = populateMap(capacity);
        this.capacity = SEGMENT_COUNT * capacity;
        this.atomicSize = new AtomicInteger(0);
    }

    @Override
    public V put(K k, V v) {
        final var segment = this.segments.get(segmentNo(k));
        return segment.put(k, v);
    }

    @Override
    public V get(K k) {
        final var segment = this.segments.get(segmentNo(k));
        return segment.get(k);
    }

    @Override
    public V evict(K k) {
        final var segment = this.segments.get(segmentNo(k));
        return segment.evict(k);
    }

    public int size() {
        return atomicSize.get();
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    public V getMostRecentlyUsed() {
        throw new UnsupportedOperationException();
    }

    public V getLeastRecentlyUsed() {
        throw new UnsupportedOperationException();
    }

    public V evictLeastRecentlyUsed() {
        throw new UnsupportedOperationException();
    }

    public V evictMostRecentlyUsed() {
        throw new UnsupportedOperationException();
    }

    static int segmentNo(Object o){
        return Math.abs(o.hashCode() % SEGMENT_COUNT);
    }

     Map<Integer, Segment<K, V>> populateMap(int capacity){
        final Map<Integer, Segment<K, V>> map = new ConcurrentHashMap<>();
        for (int i = 0; i < SEGMENT_COUNT; i++){
            map.put(i, new Segment<>(this, capacity));
        }

        return unmodifiableMap(map);
    }

    static class Segment<K, V>{
        private final LRUCache<K, V> segment;
        private final ConcurrentLRUCache<K, V> cache;

        public Segment(ConcurrentLRUCache<K, V> cache) {
            this.cache = cache;
            this.segment = new SynchronizedLRUCache<>();
        }

        public Segment(ConcurrentLRUCache<K, V> cache, int capacity) {
            this.cache = cache;
            this.segment = new SynchronizedLRUCache<>(capacity);
        }

        public V put(K k, V v) {
            V val = segment.put(k, v);
            if(val == null) this.cache.atomicSize.incrementAndGet(); //If there was no previous entry increment
            return val;
        }

        public V get(K k) {
            return segment.get(k);
        }

        public V evict(K k) {
            final V val = segment.evict(k);
            if (val != null) cache.atomicSize.decrementAndGet();
            return val;
        }

    }
}
