package com.github.kusoroadeolu.vicutils.concurrent.mutex;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/*
* Non Goals
* Making this mutex production ready
* Making this mutex have all the properties of the @Lock interface
* Making this mutex performant
*
* Goals
* Making this mutex correct in the sense you can lock and unlock it and the goals listed before
* */

/**
 * A mutex implementation using a concurrent lock free de-queue and CAS semantics.
 * </br> This mutex doesn't support conditions.
 * </br> It's simply a mini impl that's been brewing in my mind for a while. This mutex has some thread starvation issues though nothing too crazy
 * */
/*States: 0 -> unacquired, 1 -> releasing, 2 acquired
* Invariants.
* No two threads can ever hold this mutex
* Only the holder should be able to modify 'next'
* The state of this mutex can either be 0, 1 or 2
* No two threads can overwrite the holder variable. This is enforced by ensuring the holder at release is written before the state is reset
* */

public class Mutex {
    @State
    private final AtomicReference<Integer> state = new AtomicReference<>(0); //Only on thread can hold this at a time
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();
    private volatile Thread holder; //Just for reentrancy, preventing releases, not the actual @state
    private volatile Thread next; //This is just a signal not a fairness guarantee
    private int acquires;


    /* Check if its state is not acquired, if not, add to the queue and park the thread else, set the thread as the mutex's holder
      The while loop in this implementation, is for, in the case, a waiting thread is unparked, but another thread has already modified the state,
      the waiting thread will check the condition again, before being reparked
     */
    public void acquire()  {
        Thread t = Thread.currentThread();
        if (t.equals(holder)) {
            ++acquires;
            return;
        }

        boolean added = false;
        if (!state.compareAndSet(0, 2)){
            waiters.add(t); // If the 'next' thread failed to acquire the lock, add it to the beginning of the queue, to mimic a pseudo fairness guarantee
            added = true;
            do{
                while (state.get().equals(1)){
                    Thread.onSpinWait(); //wait for the holder to release, assuming it doesn't take too long
                }

                if (t.equals(next)) {
                    continue; //Means we're the next in line, return to reacquire the lock, a thread could reacquire before us though
                }

                LockSupport.park();
            }while (!state.compareAndSet(0, 2));
        }


        holder = t;
        next = null; //If the lock was successfully acquired always set next to null, to prevent infinite loops
        if (added) waiters.remove(t); //Only a thread can remove itself from the queue, also added is just to prevent O(n) ops everytime this is acquired


    }

    /*
    * To release the mutex, check if the holder is null, of the holder is null, then throw an IllegalMonitorEx,
    * Then loop through the concurrent queue, looking for non-null waiters, if found, unpark the waiter and then reset the lock's state
    * */
    public void release(){
        if (holder == null || !holder.equals(Thread.currentThread())) throw new IllegalMonitorStateException();
        if (--acquires > 0) return;

        state.set(1); //Mark as releasing
        Thread next;
        if ((next = waiters.peek()) != null){
            this.next = next;
            LockSupport.unpark(next);
        }

        state.set(0);
        holder = null; //Happens before, a write to a volatile variable happens before subsequent reads,
        // so no thread can ever sneak through. Writes to volatile variables also ensure subsequent previous writes to shared fields are made visible to other threads before the write
        // though idk how that's useful here

    }

    //Return the current holder, can return null
    public Thread holder(){
        return holder;
    }



}
