package com.github.kusoroadeolu.vicutils.ds.lrucache;

import com.github.kusoroadeolu.vicutils.ds.LRUCache;

public interface SegmentedLRUCache<K, V> extends LRUCache<K, V> {
    V put(K k, V v);

    V get(K k);

    V evict(K k);

    int size();

    int capacity();
}
