package com.github.kusoroadeolu.vicutils.ds;

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
public class LRUCache<K, V> {
    private final Map<K, V> cache;
    private final int capacity;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public LRUCache(){
        this(1 << 8);
    }

    public LRUCache(int capacity){
        this(new HashMap<>(capacity), capacity);
    }

    LRUCache(Map<K, V> cache, int capacity) {
        this.cache = cache;
        this.capacity = capacity;
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        this.updateNodePointers(this.head, this.tail);
    }


    public boolean put(K k, V v){
        validateKey(k);
        validateVoid(v);
        int size = this.cache.size();
        if (++size > this.capacity) return false;
        V exists = this.cache.put(k, v);
        Node<K, V> newHeadNode = null;
        if (exists == null){ //If this mapping did not exist before, create a new Node and set it as most recently used
             newHeadNode = new Node<>(k, v);
        }else{ //We want to find the node and then set it as the most recently used
            Node<K, V> node = this.head;
            while (node != null){
                if (k.equals(node.k)){
                    newHeadNode = node;
                    break;
                }

                node = node.tail;
            }
        }

        this.updateMostRecentlyUsed(newHeadNode);
        return true;
    }

    public V get(K k){
        validateKey(k);
        if (this.size() < 1) return null;
        final V v = this.cache.get(k);
        if (v == null) return null;
        Node<K, V> node = this.head;
        while (node != null){ //Loop to look for a node with K key
            if (k.equals(node.k)){
                this.updateMostRecentlyUsed(node);
                break;
            }
            node = node.tail;
        }

        return v;
    }


    //Remove a node N.
    //Then set N#head to it's N#tail and N#tail to it's N#tail
    public V evict(K k){
        validateKey(k);
        if (this.size() < 1) return null;
        final V v = this.cache.remove(k);
        if (v == null) return null;
        Node<K, V> node = this.head;
        while (node != null){
            if (k.equals(node.k)){
                Node<K, V> head = node.head;
                Node<K, V> tail = node.tail;
                this.updateNodePointers(head, tail);
                break;
            }

            node = node.tail;
        }
        return v;
    }

    //Can return null if there's nothing in the cache
    public V getMostRecentlyUsed(){
        return this.head.tail.v;
    }

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

    void updateMostRecentlyUsed(Node<K, V> newNode){
        Node<K, V> oldNode = this.head.tail;
        this.updateNodePointers(this.head, newNode);
        this.updateNodePointers(newNode, oldNode);
    }

    //Set the head's bottom to tail
    //Same as the tail
    void updateNodePointers(Node<K, V> head, Node<K, V> tail){
        head.tail = tail;
        tail.head = head;
    }

    static <K>void validateKey(K k){
        requireNonNull(k, "Key cannot be null");
    }

    static <V>void validateVoid(V v){
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
