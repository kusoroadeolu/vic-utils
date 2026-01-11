package com.github.kusoroadeolu.vicutils.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void shouldShiftHeadAndTailOnKeyPut(){
        LRUCache<String, Integer> cache = new LRUCache<>();
        boolean added = cache.put("1", 1);

        assertTrue(added);
        assertEquals(1, cache.getMostRecentlyUsed());
        assertEquals(1, cache.getLeastRecentlyUsed());
    }

    @Test
    void onPut_mostRecentlyUsed_shouldEqual2(){
        LRUCache<String, Integer> cache = new LRUCache<>();
        cache.put("1", 1);
        cache.put("2", 2);

        assertEquals(2, cache.getMostRecentlyUsed());
        assertEquals(1, cache.getLeastRecentlyUsed());
    }

    @Test
    void onEvict_mostRecentlyUsed_shouldEquals1(){
        LRUCache<String, Integer> cache = new LRUCache<>();
        cache.put("1", 1);
        cache.put("2", 2);
        cache.evict("2");

        assertEquals(1, cache.getMostRecentlyUsed());
    }

    @Test
    void onEvict_ensureCacheEmpty(){
        LRUCache<String, Integer> cache = new LRUCache<>();
        cache.put("1", 1);
        cache.evict("1");

        assertEquals(0, cache.size());
        assertEquals(cache.head().tail, cache.tail());
    }

    @Test
    void verifyLeastRecentlyUsed(){
        LRUCache<String, Integer> cache = new LRUCache<>();
        cache.put("1", 1);
        cache.put("2", 2);
        cache.put("3", 4);

        assertEquals(1, cache.getLeastRecentlyUsed());
    }






}