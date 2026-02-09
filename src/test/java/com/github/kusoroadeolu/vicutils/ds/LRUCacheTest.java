package com.github.kusoroadeolu.vicutils.ds;

import com.github.kusoroadeolu.vicutils.ds.lrucache.SequentialLRUCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void shouldShiftHeadAndTailOnKeyPut(){
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>();
        var added = cache.put("1", 1);

        assertNull(added);
        assertEquals(1, cache.getMostRecentlyUsed());
        assertEquals(1, cache.getLeastRecentlyUsed());
    }

    @Test
    void onPut_mostRecentlyUsed_shouldEqual2(){
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>();
        cache.put("1", 1);
        cache.put("2", 2);

        assertEquals(2, cache.getMostRecentlyUsed());
        assertEquals(1, cache.getLeastRecentlyUsed());
    }

    @Test
    void onEvict_mostRecentlyUsed_shouldEquals1(){
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>();
        cache.put("1", 1);
        cache.put("2", 2);
        cache.evict("2");

        assertEquals(1, cache.getMostRecentlyUsed());
    }

    @Test
    void onEvict_ensureCacheEmpty(){
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>();
        cache.put("1", 1);
        cache.evict("1");

        assertEquals(0, cache.size());
        assertEquals(cache.head().tail, cache.tail());
    }

    @Test
    void verifyLeastRecentlyUsed(){
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>();
        cache.put("1", 1);
        cache.put("2", 2);
        cache.put("3", 4);

        assertEquals(1, cache.getLeastRecentlyUsed());
    }

    @Test
    void verifyEvictsLeastRecentlyUsed_onCacheFull(){
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>(3);
        cache.put("1", 1);
        cache.put("2", 2);
        cache.put("3", 4);
        cache.put("4", 5);

        assertEquals(2, cache.getLeastRecentlyUsed());
    }

    @Test
    void onGet_shouldUpdateMostRecentlyUsed() {
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>(3);
        cache.put("1", 1);
        cache.put("2", 2);
        cache.put("3", 3);

        cache.get("1"); // Access "1", should move it to front

        assertEquals(1, cache.getMostRecentlyUsed());
        assertEquals(2, cache.getLeastRecentlyUsed());
    }

    @Test
    void onGet_preventEvictionOfAccessedKey() {
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>(3);
        cache.put("1", 1);
        cache.put("2", 2);
        cache.put("3", 3);

        cache.get("1"); // Keep "1" alive
        cache.put("4", 4); // Should evict "2", not "1"

        assertNotNull(cache.get("1"));
        assertNull(cache.get("2"));
    }

    @Test
    void onPutExistingKey_shouldUpdateValue() {
        SequentialLRUCache<String, Integer> cache = new SequentialLRUCache<>();
        cache.put("1", 1);
        Integer old = cache.put("1", 100);

        assertEquals(1, old);
        assertEquals(100, cache.get("1"));
        assertEquals(1, cache.size()); // Should still be 1 item
    }

    @Test
    void leetcodeStyleTest() {
        SequentialLRUCache<Integer, Integer> cache = new SequentialLRUCache<>(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));
        cache.put(3, 3);
        assertNull(cache.get(2));
        cache.put(4, 4);
        assertNull(cache.get(1));
        assertEquals(3, cache.get(3));
        assertEquals(4, cache.get(4));
    }




}