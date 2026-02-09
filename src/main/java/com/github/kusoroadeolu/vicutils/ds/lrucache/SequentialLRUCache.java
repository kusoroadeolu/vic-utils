package com.github.kusoroadeolu.vicutils.ds.lrucache;

import com.github.kusoroadeolu.vicutils.ds.LRUCache;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Invariants
 * Head must not have a `head` node and Tail must not have a `tail` node
 * Cache#put does not allow null keys or values
 * Cache#put has a fixed final size
 * Cache#put does not allow its size to exceed its capacity
 * Cache#put returns the previous value associated if the value was successfully added, false otherwise
 * Cache#get returns V if the value exists, otherwise null
 * */

/*
* H
* |
* bottom node
* | -> in between
* top node
* |
* Tail
* */
public class SequentialLRUCache<K, V> implements LRUCache<K, V> {
    private final Map<K, Node<K, V>> cache;
    private final int capacity;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public SequentialLRUCache(){
        this(1 << 8);
    }

    public SequentialLRUCache(int capacity){
        this(new HashMap<>(capacity), capacity);
    }

    SequentialLRUCache(Map<K, Node<K, V>> cache, int capacity) {
        this.capacity = capacity;
        this.cache = cache;
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        this.updateNodePointers(this.head, this.tail);
    }

    public V put(K k, V v){
        validateKey(k);
        validateValue(v);
        Node<K, V> oldNode = this.cache.get(k);

        if (oldNode == null && this.size() >= this.capacity) {
            this.evictLeastRecentlyUsed();
        }

        Node<K, V> newNode = new Node<>(k, v);
        this.cache.put(k, newNode);
        if (oldNode != null){
            this.updateNodePointers(oldNode.head, oldNode.tail);
            this.dereferenceNode(oldNode);
        }

        this.updateMostRecentlyUsed(newNode);
        return oldNode != null ? oldNode.v : null;
    }


    public V get(K k){
        validateKey(k);
        if (this.size() < 1) return null;
        final Node<K, V> node = this.cache.get(k);
        if (node == null) return null;
        this.updateMostRecentlyUsed(node);
        return node.v;
    }


    //Remove a node N.
    //Then set N#head to it's N#tail and N#tail to it's N#tail
    @Override
    public V evict(K k){
        validateKey(k);
        if (this.size() < 1) return null;
        final Node<K, V> node = this.cache.remove(k);
        if (node == null) return null;
        this.updateNodePointers(node.head, node.tail);
        this.dereferenceNode(node);
        return node.v;
    }

    //Can return null if there's nothing in the cache
    public V getMostRecentlyUsed(){
        return this.head.tail.v;
    }

    //Can return null if there's nothing in the cache
    public V getLeastRecentlyUsed(){
        return this.tail.head.v;
    }

    public int size(){
        return this.cache.size();
    }

    public int capacity(){
        return this.capacity;
    }


    //Can throw an illegal argument ex, if there's nothing in the cache
    public V evictLeastRecentlyUsed(){
        return this.evict(this.tail.head.k); //Evict the cache's tail's head
    }

    //Can throw an illegal argument ex, if there's nothing in the cache
    public V evictMostRecentlyUsed(){
        return this.evict(this.head.tail.k);
    }

    //Set the head's tail to the new node and the new node's head to the head
    void updateMostRecentlyUsed(Node<K, V> node){
        if (node.head != null && node.tail != null){ //Update the node's head and tails
            this.updateNodePointers(node.head, node.tail);
        }

        Node<K, V> oldNode = this.head.tail;
        this.updateNodePointers(this.head, node);
        this.updateNodePointers(node, oldNode);
    }

    //Set the head's tail to tail
    //Same as the tail's head to head
    void updateNodePointers(Node<K, V> head, Node<K, V> tail){
        head.tail = tail;
        tail.head = head;
    }

    void dereferenceNode(Node<K, V> node){
        node.head = null;
        node.tail = null;
    }

    static <K>void validateKey(K k){
        requireNonNull(k, "Key cannot be null");
    }

    static <V>void validateValue(V v){
        requireNonNull(v, "Value cannot be null");
    }

    Node<K, V> head(){
        return this.head;
    }

    Node<K, V> tail(){
        return this.tail;
    }


    static final class Node<K, V>{
        final K k;
        final V v;
        Node<K, V> head;
        Node<K, V> tail;

        public Node(K k, V v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "k=" + k +
                    ", v=" + v +
                    '}';
        }
    }
}
