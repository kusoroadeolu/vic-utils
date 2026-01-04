package com.github.kusoroadeolu.vicutils.concurrent.mutex;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/*
* Non Goals
* Making this mutex reentrant
* Making this mutex production ready
* Making this mutex have all the properties of the @Lock interface
* Making this mutex performant
*
* Goals
* Making this mutex correct in the sense you can lock and unlock it and the goals listed before
* */

/**
 * A naive mutex implementation using a concurrent lock free queue and CAS semantics.
 * </br> This mutex doesn't support conditions, also this mutex has no happens before guarantees that I can guarantee you of
 * </br> It's simply a mini impl that's been brewing in my mind for a while. This thread doesn't support reentrancy yet and has some thread starvation issues
 * */
//States: 0 -> unacquired, 1 -> releasing, 2 acquired
/* Invariants.
* No two threads can ever hold this mutex
* The state of this mutex can either be 0, 1 or 2
* No two threads can overwrite the holder variable. This is enforced by ensuring the holder at release is written before the state is reset
* */

public class NaiveMutex {
    private final AtomicReference<Integer> state = new AtomicReference<>(0); //Only on thread can hold this at a time
    private final ConcurrentLinkedQueue<Thread> waiters = new ConcurrentLinkedQueue<>();
    private volatile Thread holder;
    private volatile Thread next;


    /* Check if its state is not acquired, if not, add to the queue and park the thread else, set the thread as the mutex's holder
      The while loop in this implementation, is for, in the case, a waiting thread is unparked, but another thread has already modified the state,
      the waiting thread will check the condition again, before being reparked
     */
    public void acquire()  {
        Thread t = Thread.currentThread();
        while (!state.compareAndSet(0, 2)){
            waiters.remove(t); //Remove in the case the thread failed to acquire the lock when it was next
            waiters.add(t);

            while (state.get() == 1){
                Thread.onSpinWait(); //wait for the holder to release, shouldn't take too long
            }

            if (t.equals(this.next)) {
                continue; //Means we're the next in line, return to reacquire the lock, a thread could reacquire before us though
            }


            LockSupport.park();
        }

        holder = t;

    }

    /*
    * To release the mutex, check if the holder is null, of the holder is null, then throw an IllegalMonitorEx,
    * Then loop through the concurrent queue, looking for non-null waiters, if found, unpark the waiter and then reset the the lock's state
    * */
    public void release(){
        if (holder == null || !holder.equals(Thread.currentThread())) throw new IllegalMonitorStateException();
        state.set(1); //Mark as releasing
        Thread next;
        if ((next = waiters.poll()) != null){
            LockSupport.unpark(next);
        }

        this.next = next;
        state.set(0);
        holder = null;
    }

    //Return the current holder, can return null
    public Thread holder(){
        return holder;
    }



}
