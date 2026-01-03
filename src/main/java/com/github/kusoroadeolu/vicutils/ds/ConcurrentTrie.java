package com.github.kusoroadeolu.vicutils.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


// A concurrent trie that allows multiple threads to perform operations on it concurrently
/*Invariants
* No two threads can ever call @add or @remove concurrently if they try to modify the same char in the map
* @size is not weakly consistent and cannot be trusted fully, though it is good for estimating the amount of words that have been inserted
* Multiple threads can read from this trie concurrently
*
* I am not using a concurrent hashmap for the trie head because each write is serialized using @writeLock, hence only one thread can modify it at a time
* and once written all nodes are guaranteed to be safely published and made visible to other threads once the lock is released
* */
// I did try copy on write, but I realized deep copying a full trie is definitely not worth it lol and will be very memory heavy
public class ConcurrentTrie extends SequentialTrie implements Trie{
    private final ConcurrentHashMap<Character, ReadWriteLock> lockMap;
    private final AtomicInteger size;
    public ConcurrentTrie(Map<Character, Map<Character, Node>> heads, int size) {
        super(heads, size);
        this.lockMap = new ConcurrentHashMap<>();
        this.size = new AtomicInteger();
    }

    public ConcurrentTrie() {
        this(new ConcurrentHashMap<>(), 0);
    }

    public boolean add(String word){
        this.validateWord(word);
        word = word.toLowerCase();
        char c = word.charAt(0);
        ReadWriteLock lock = this.lockMap.computeIfAbsent(c, _ -> new ReentrantReadWriteLock());


        lock.writeLock().lock();
        try {
            if (this.containsExact(word)) return false;
            size.incrementAndGet(); //increment the size immediately, would be useful for CAS loops where i'll be checking the size rather than the reference itself
            if (word.length() == 1) this.heads.putIfAbsent(c, new HashMap<>());
            else {
                final var map = this.addHead(word);
                //Next character should always be 1
                this.insert(word, map, 1);
            }

            return true;
        }finally {
            lock.writeLock().unlock();
        }

    }

    public boolean remove(String word){
        this.validateWord(word);
        word = word.toLowerCase();
        char c = word.charAt(0);

        ReadWriteLock lock = this.lockMap.computeIfAbsent(c, _ -> new ReentrantReadWriteLock());

        lock.writeLock().lock();
        try {
            if (!this.containsExact(word)) return false;
            size.decrementAndGet();
            Map<Character, Node> map = heads.get(c);
            //Index should be one here since we're passing the node map
            int removeAt = this.findUniparentalOldestNode(word, map, 1, 0);
            this.removeNodes(map, word, 1, removeAt);
            return true;
        }finally {
            lock.writeLock().unlock();
        }

    }

    public List<String> startsWith(String prefix){
        return this.findPrefixes(prefix, false);
    }

    //Checks if a word contains words under this @prefix
    public boolean containsPrefix(String prefix){
        return this.findPrefixes(prefix, true).size() > 1;
    }

    public boolean containsExact(String word){
        this.validateWord(word);
        word = word.toLowerCase();
        char c = word.charAt(0);
        ReadWriteLock lock = this.lockMap.computeIfAbsent(c, _ -> new ReentrantReadWriteLock());
        //Don't need to lock on this op si


        lock.readLock().lock();
        try {
            Map<Character, Node> currMap = this.heads.get(c);
            if (word.length() == 1) return this.heads.containsKey(c);
            if (currMap == null) return false;
            Node node = null;
            for (int i = 1; i < word.length(); i++){
                node = currMap.get(word.charAt(i));
                if (node == null) return false; //Ensure this char exists
                currMap = node.children();
            }
            return node.isWordEnd();
        }finally {
            lock.readLock().unlock();
        }
    }

    public int size(){
        return this.size.get();
    }

    List<String> findPrefixes(String prefix, boolean shouldBreak){
        this.validateWord(prefix);
        prefix = prefix.toLowerCase();
        List<String> list = new ArrayList<>();
        char c = prefix.charAt(0);
        ReadWriteLock lock = this.lockMap.computeIfAbsent(c, _ -> new ReentrantReadWriteLock());

        lock.readLock().lock();
        try {
            Map<Character, Node> nodes = this.heads.get(c);
            if (nodes == null) return list;

            String s = Character.toString(c);
            list.add(s);
            this.findWords(nodes, list, s, prefix.length() ,shouldBreak);
            return list;
        }finally {
            lock.readLock().unlock();
        }
    }
}
