package com.github.kusoroadeolu.vicutils.ds.counter;


//I have 5 ideas for these lock based counters
/*
- A big fat lock that syncs everything
- 2 locks, 2 integers, one increment, one decrement value, on get we hold both locks incrlock, decrlock and subtract incr and decr, and on reset we hold them in the same order to prevent deadlocks
- We could have a striped counter using an array up to 1 << 4, so 8 buckets, each holding a lock and a count, threads incrementing are moduloed based on their hashcode and the value is gotten from the array. Reads are thread safe automatically since we aren't modifying the array itself only the object it holds which is protected by a lock4
- A single lock free counter
- A striped lock free counter
 */

public interface Counter {
    void increment();
    void decrement();
    void increment(long count);
    void decrement(long count);
    long get();
    void reset();
}
