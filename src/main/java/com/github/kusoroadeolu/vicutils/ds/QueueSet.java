package com.github.kusoroadeolu.vicutils.ds;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class QueueSet<E> implements Queue<E> {
    private final Set<E> queue;
    private final int capacity;

    public QueueSet() {
        this(1 << 4);
    }

    public QueueSet(int capacity){
        this.queue = new LinkedHashSet<>(capacity);
        this.capacity = capacity;
    }

    public boolean add(E e) {
        final var added = this.offer(e);
        if (!added) throw new  IllegalStateException();
        else return true;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        var size = queue.size() + 1;
        if (size >= capacity) return false;
        else return this.queue.add(e);
    }

    @Override
    public E remove() {
        if (this.isEmpty()) throw new NoSuchElementException();
        else return this.poll();
    }

    @Override
    public E poll() {
        var opt= this.queue.stream().findFirst();
        opt.ifPresent(this.queue::remove);
        return opt.get();
    }

    @Override
    public E element() {
        return this.queue.stream().findFirst().get();
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}
