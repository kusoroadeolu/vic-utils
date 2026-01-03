package com.github.kusoroadeolu.vicutils.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SynchronizedTrie extends SequentialTrie implements Trie{
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock rl = rwl.readLock();
    private final Lock wl = rwl.writeLock();
    private volatile int size;
    public SynchronizedTrie(Map<Character, Map<Character, Trie.Node>> heads, int size) {
        super(heads, size);

    }

    public SynchronizedTrie() {
        this(new HashMap<>(), 0);
    }

    public boolean add(String word){
        this.validateWord(word);
        word = word.toLowerCase();
        char c = word.charAt(0);
        wl.lock();
        try {
            if (this.containsExact(word)) return false;
            ++size;//increment the size immediately, would be useful for CAS loops where i'll be checking the size rather than the reference itself
            if (word.length() == 1) this.heads.putIfAbsent(c, new HashMap<>());
            else {
                final var map = this.addHead(word);
                //Next character should always be 1
                this.insert(word, map, 1);
            }

            return true;
        }finally {
            wl.unlock();
        }

    }

    public boolean remove(String word){
        this.validateWord(word);
        word = word.toLowerCase();
        char c = word.charAt(0);

        wl.lock();
        try {
            if (!this.containsExact(word)) return false;
            --size;
            Map<Character, Trie.Node> map = heads.get(c);
            //Index should be one here since we're passing the node map
            int removeAt = this.findUniparentalOldestNode(word, map, 1, 0);
            this.removeNodes(map, word, 1, removeAt);
            return true;
        }finally {
            wl.unlock();
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
        char c = word.charAt(0);//Don't need to lock on this op si


        rl.lock();
        try {
            Map<Character, Trie.Node> currMap = this.heads.get(c);
            if (word.length() == 1) return this.heads.containsKey(c);
            if (currMap == null) return false;
            Trie.Node node = null;
            for (int i = 1; i < word.length(); i++){
                node = currMap.get(word.charAt(i));
                if (node == null) return false; //Ensure this char exists
                currMap = node.children();
            }
            return node.isWordEnd();
        }finally {
            rl.unlock();
        }

        //Ensure the final node is the word end
    }

    public int size(){
        return this.size;
    }

    List<String> findPrefixes(String prefix, boolean shouldBreak){
        this.validateWord(prefix);
        prefix = prefix.toLowerCase();
        List<String> list = new ArrayList<>();
        char c = prefix.charAt(0);

        rl.lock();
        try {
            Map<Character, Trie.Node> nodes = this.heads.get(c);
            if (nodes == null) return list;

            String s = Character.toString(c);
            list.add(s);
            this.findWords(nodes, list, s, prefix.length() ,shouldBreak);
            return list;
        }finally {
            rl.unlock();
        }
    }
}
