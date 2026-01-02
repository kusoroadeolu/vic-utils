package com.github.kusoroadeolu.vicutils.ds;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class Trie {
    //A map of a node (a character) and it's children nodes, we're assuming this map is a list of nodes, though it exists solely for 0(1) lookups and to simplify things
    private final Map<Character, Map<Character, Node>> heads = new HashMap<>();
    private int size;

    public boolean add(String word){
        this.validateWord(word);
        if (this.containsExact(word)) return false;
        word = word.toLowerCase();
        ++size; //increment the size immediately, would be useful for CAS loops where i'll be checking the size rather than the reference itself
        if (word.length() == 1) this.heads.putIfAbsent(word.charAt(0), new HashMap<>());
        else {
            final var map = this.addHead(word);
            //Next character should always be 1
            this.insert(word, map, 1);
        }

        return true;
    }

    public boolean remove(String word){
        this.validateWord(word);
        word = word.toLowerCase();

        if (!this.containsExact(word)) return false;
        --size;
        char c = word.charAt(0);
        Map<Character, Node> map = heads.get(c);
        //Index should be one here since we're passing the node map
        int removeAt = this.findUniparentalOldestNode(word, map, 1, 0);
        this.removeNodes(map, word, 1, removeAt);
        return true;
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
         if (word.length() == 1) return this.heads.containsKey(word.charAt(0));

         Map<Character, Node> currMap = this.heads.get(word.charAt(0));
         if (currMap == null) return false;

         Node node = null;
         for (int i = 1; i < word.length(); i++){
             node = currMap.get(word.charAt(i));
             if (node == null) return false; //Ensure this char exists
             currMap = node.children;
         }

         return node.isWordEnd; //Ensure the final node is the word end
     }



     //
     private List<String> findPrefixes(String prefix, boolean shouldBreak){
         this.validateWord(prefix);
         prefix = prefix.toLowerCase();
         List<String> list = new ArrayList<>();
         char c = prefix.charAt(0);
         Map<Character, Node> nodes = this.heads.get(c);
         if (nodes == null) return list;

         String s = Character.toString(c);
         list.add(s);
         this.findWords(nodes, list, s, prefix.length() ,shouldBreak);
         return list;
     }

     private void removeNodes(Map<Character, Node> map, String word, int index, int removeAt){
        int len = word.length();
        if (len == index) return;
        char c = word.charAt(index);
        Node node = map.get(c);
        if (removeAt == -1){
            if ((len - 1) == index) node.setWordEnd(false); //Means we're at the final index of this word node
        }else {
            //The index is
            if ((index == removeAt)){ //Remove the node from the parent.  is indicating that this is the parent that we want to remove the child from
                map.remove(c);
                return;
            }
        }

         this.removeNodes(node.children, word, ++index, removeAt);
     }

     // Remove from is what we're modifying, we're using index to keep track of the recursive depth
     private int findUniparentalOldestNode(String word, Map<Character, Node> map, int index, int removeAt){
        int len = word.length();
        if (index == len) { //If the index is equals to length, means we've reached the end of this string
            if (!map.isEmpty()) return -1; //-1 means don't remove anything, because this node has children, just
            else return removeAt;
        }
        char c = word.charAt(index);
        Node node = map.get(c); //Map will always contain c, since, we've checked before
        if (node.children.size() <= 1) removeAt = index;
        return findUniparentalOldestNode(word, node.children, ++index, removeAt);
     }


    //Add the first char of this word to the map if it doesn't exist, and return its map of nodes
    Map<Character, Node> addHead(String word){
        char c = word.charAt(0);
        Map<Character, Node> map = this.heads.get(c);
        if (map == null){
            map = new HashMap<>();
            this.heads.put(c, map);
            return map;
        }

        return map;
    }

    //Recursively iterates through each node, adding valid inserted words into the list. 'Should break', returns when the first valid prefix is found
    //Len, the length of the original word/prefix passed
     void findWords(Map<Character, Node> nodes, List<String> words, String prefix, int len, boolean shouldBreak){
        List<Node> nodeList = List.copyOf(nodes.values());
        for (Node n : nodeList){
            String s = prefix + n.c;
            if (n.isWordEnd && s.length() >= len){
                words.add(s);
                if (shouldBreak) return; //Return once you find the first word
            }
            this.findWords(n.children, words, s, len ,shouldBreak);
        }
     }

    //Recursively walks through a node, checking extra nodes, and
    void insert(String word, Map<Character, Node> parentMap, int index){
        int len = word.length();
        if (index == len) return; //If the index is out of bounds return
        boolean isWordEnd = index == len - 1;
        char c = word.charAt(index);
        Node cNode = parentMap.computeIfAbsent(c, _ -> new Node(c, new HashMap<>(), isWordEnd));
        if (!cNode.children.isEmpty() && isWordEnd) cNode.setWordEnd(true); //If this is a prefix of an old insert, ensure we set that this is a word
        this.insert(word, cNode.children(), ++index);
    }

    void validateWord(String s){
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("word != null && word != \"\"");
    }



     static class Node {
        private final char c;
        private final Map<Character, Node> children;
        private boolean isWordEnd;

        public Node(char c, Map<Character, Node> children, boolean isWordEnd) {
            this.c = c;
            this.children = children;
            this.isWordEnd = isWordEnd;
        }



        // Getters and Setters
        public char getC() { return c; }
        public Map<Character, Node> children() { return children; }
        public boolean isWordEnd() { return isWordEnd; }
        public void setWordEnd(boolean wordEnd) { isWordEnd = wordEnd; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node node)) return false;
            return c == node.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(c);
        }

         @Override
         public String toString() {
             return "Node{" +
                     "c=" + c +
                     ", children=" + children +
                     ", isWordEnd=" + isWordEnd +
                     '}';
         }
     }

    public static void main(String[] args){
        Trie trie = new Trie();

        // Test 1: Basic Addition & Retrieval
        trie.add("apple");
        trie.add("app");
        trie.add("ball");

        System.out.println("Contains apple: " + trie.containsExact("apple")); // Expect true
        System.out.println("Starts with 'app': " + trie.startsWith("app"));   // Expect [a, app, apple]

        // Test 2: The "Beefy" Deletion (Pruning)
        // "ball" is unique, so it should be fully pruned from 'heads'
        trie.remove("ball");
        System.out.println("Contains ball: " + trie.containsExact("ball"));   // Expect false

        // Test 3: Shared Branch Deletion
        // Removing "app" should set isWordEnd=false but leave "apple" intact
        trie.remove("app");
        System.out.println("Contains app: " + trie.containsExact("app"));     // Expect false
        System.out.println("Contains apple: " + trie.containsExact("apple")); // Expect true

        // Test 4: Single Letter Edge Case
        trie.add("a");
        System.out.println("Contains 'a': " + trie.containsExact("a"));

    }
}
