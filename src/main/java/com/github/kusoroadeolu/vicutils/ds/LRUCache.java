package com.github.kusoroadeolu.vicutils.ds;

public interface LRUCache<K, V> {
    V put(K k, V v);

    V get(K k);

    //Remove a node N.
    //Then set N#head to it's N#tail and N#tail to it's N#tail
    V evict(K k);

    //Can return null if there's nothing in the cache
    V getMostRecentlyUsed();

    //Can return null if there's nothing in the cache
    V getLeastRecentlyUsed();

    int size();

    int capacity();

    //Can throw an illegal argument ex, if there's nothing in the cache
    V evictLeastRecentlyUsed();

    //Can throw an illegal argument ex, if there's nothing in the cache
    V evictMostRecentlyUsed();
}
