package com.github.kusoroadeolu.vicutils.ds.nonconcurrent;

import java.util.*;

public class QueueSet<E> implements Queue<E>, Set<E> {
    private final Set<E> queue;
    private final int capacity;
    private static final int DEFAULT_QUEUE_SIZE = 1 << 4;

    public QueueSet() {
        this(DEFAULT_QUEUE_SIZE);
    }

    public QueueSet(int capacity){
        this.queue = new LinkedHashSet<>(capacity);
        this.capacity = capacity;
    }

    public boolean add(E e) {
        final var added = this.offer(e);
        if (!added) throw new IllegalStateException();
        else return true;
    }

    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        else if (queue.size() >= capacity) return false;
        else return this.queue.add(e);
    }

    public E remove() {
        if (this.isEmpty()) throw new NoSuchElementException();
        else return this.poll();
    }

    @Override
    public E poll() {
        var opt= this.findFirst();
        opt.ifPresent(this.queue::remove);
        return opt.orElse(null);
    }

    @Override
    public E element() {
       E val = this.peek();
       if (val == null) throw new NoSuchElementException();
       return val;
    }

    @Override
    public E peek() {
        return this.findFirst().orElse(null);
    }

    @Override
    public int size() {
        return this.queue.size();
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.queue.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return this.queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.queue.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return this.queue.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.queue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int count = 0;
        for (E o : c){
           if(this.offer(o)) ++count;
        }

        return count == c.size();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.queue.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.queue.retainAll(c);
    }

    @Override
    public void clear() {
        this.queue.clear();
    }

    Optional<E> findFirst(){
        var it = queue.iterator();
        return it.hasNext() ? Optional.of(it.next()) : Optional.empty();
    }
}
