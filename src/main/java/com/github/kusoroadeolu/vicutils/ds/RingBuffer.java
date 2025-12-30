package com.github.kusoroadeolu.vicutils.ds;

import java.util.Iterator;
import java.util.function.Consumer;

// A simple ring buffer without ceremony
public class RingBuffer<T> implements Iterable<T>{
    private final static int FIRST_INDEX = 0;
    private final Object[] buf;
    private final int cap;
    private int pos;
    private boolean hasFilled = false; //Checks if the buffer has filled up at least once, just to track when the pos is the HEAD

    public RingBuffer(int capacity) {
        this.cap = capacity;
        this.buf = new Object[cap];
    }


    /**
     * Add an element and remove the oldest element in the buffer, if the buffer is full or the head was removed
     * @param val The value to add
     * @return The value removed, returns null if no value was removed
     * */
    @SuppressWarnings("unchecked")
    public T add(T val){
        this.resetPos();
        final T old = (T) this.buf[pos];
        this.buf[pos] = val;
        pos++; //Move the pointer to keep track of the next head to be overwritten
        return old;
    }

    //If the current position = the buffer's capacity, reset the position make has filled true
    void resetPos(){
        if (pos >= cap){
            pos = FIRST_INDEX;
            this.hasFilled = true;
        }
    }

    @SuppressWarnings("unchecked")
    public T get(int index){
        if (index > (this.buf.length - 1)) throw new IndexOutOfBoundsException("index > " + (this.buf.length - 1));
        return (T)this.buf[index];
    }

    @SuppressWarnings("unchecked")
    public T remove(int index){
        if (index > (this.buf.length - 1)) throw new IndexOutOfBoundsException("index > " + (this.buf.length - 1));
        T val = (T)buf[index];
        buf[index] = null;
        return val;
    }

    @SuppressWarnings("unchecked")
    public T head(){
        if (!this.hasFilled)return (T) buf[FIRST_INDEX];
        else return (T) buf[pos];
    }

    public int headIndex(){
        return hasFilled ? pos : FIRST_INDEX;
    }

    public T getFirst(){
        return this.get(FIRST_INDEX);
    }

    public T getLast(){
        int lastPos = this.pos;
        if(this.hasFilled) return this.get(cap - 1);
        else return this.get(--lastPos);
    }

    public int capacity(){
        return this.cap;
    }

    public int size(){
        return this.buf.length;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray(){
        final Object[] arr = this.buf.clone();
        return (T[])arr;
    }


    public  Iterator<T> iterator() {
        return new RingBufferIterator<>(this);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    public static class RingBufferIterator<T> implements Iterator<T>{

        private final RingBuffer<T> ringBuffer;
        private int idx = 0;

        public RingBufferIterator(RingBuffer<T> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        @Override
        public boolean hasNext() {
            return this.idx < ringBuffer.cap;
        }

        @Override
        public T next() {
            return ringBuffer.get(idx++);
        }
    }
}
