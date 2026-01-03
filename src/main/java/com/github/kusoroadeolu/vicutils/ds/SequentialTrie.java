package com.github.kusoroadeolu.vicutils.ds;

import java.util.*;

public class SequentialTrie implements Trie{
    //A map of a node (a character) and it's children nodes, we're assuming this map is a list of nodes, though it exists solely for 0(1) lookups and to simplify things
    final Map<Character, Map<Character, Node>> heads;
    int size;

    SequentialTrie(Map<Character, Map<Character, Node>> heads, int size) {
        this.heads = heads;
        this.size = size;
    }

    public SequentialTrie(){
        this(new HashMap<>(), 0);
    }

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
             currMap = node.children();
         }

         return node.isWordEnd(); //Ensure the final node is the word end
     }

     public int size(){
        return this.size;
     }

     public Trie copy(){
        return new SequentialTrie(Map.copyOf(heads), size);
     }


    //Helpers

      List<String> findPrefixes(String prefix, boolean shouldBreak){
         this.validateWord(prefix);
         prefix = prefix.toLowerCase();
         List<String> list = new ArrayList<>();
         char c = prefix.charAt(0);
         Map<Character, Node> nodes = this.heads.get(c);
         if (nodes == null) return list;

         String s = Character.toString(c);
         list.add(s);
         this.findWords(nodes, list, new StringBuilder(s), prefix.length() ,shouldBreak);
         return list;
     }

     //Recursively goes through each node
     void removeNodes(Map<Character, Node> map, String word, int index, int removeAt){
        int len = word.length();
        if (len == index) return;
        char c = word.charAt(index);
        Node node = map.get(c);
        if (removeAt == -1){
            if ((len - 1) == index) node.setWordEnd(false); //Means we're at the final index of this word node
        }else {
            //The index is at the parent index(remove at), so we just remove the character and its children
            if ((index == removeAt)){ //Remove the node from the parent is indicating that this is the parent that we want to remove the child from
                map.remove(c);
                return;
            }
        }

         this.removeNodes(node.children(), word, ++index, removeAt);
     }

     // Remove at is what we're modifying, we're using index to keep track of the recursive depth
     int findUniparentalOldestNode(String word, Map<Character, Node> map, int index, int removeAt){
        int len = word.length();
        if (index == len) { //If the index is equals to length, means we've reached the end of this string
            if (!map.isEmpty()) return -1; //-1 means don't remove anything, because this node has children
            else return removeAt;
        }
        char c = word.charAt(index);
        Node node = map.get(c); //Map will always contain c, since, we've checked before
        if (node.children().size() <= 1) removeAt = index; //If this node only has one child set remove at to the index
        return findUniparentalOldestNode(word, node.children(), ++index, removeAt);
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
     void findWords(Map<Character, Node> nodes, List<String> words, StringBuilder prefix, int len, boolean shouldBreak){
        Collection<Node> nodeList = nodes.values();
        for (Node n : nodeList){
            String s = prefix.append(n.c()).toString();
            if (n.isWordEnd() && s.length() >= len){
                words.add(s);
                if (shouldBreak) return; //Return once you find the first word
            }
            this.findWords(n.children(), words, prefix, len ,shouldBreak);
        }
     }

    //Recursively walks through a node, checking extra nodes, and
    void insert(String word, Map<Character, Node> parentMap, int index){
        int len = word.length();
        if (index == len) return; //If the index is out of bounds return
        boolean isWordEnd = index == len - 1;
        char c = word.charAt(index);
        Node cNode = parentMap.computeIfAbsent(c, _ -> new Node(c, new HashMap<>(), isWordEnd));
        if (!cNode.children().isEmpty() && isWordEnd) cNode.setWordEnd(true); //If this is a prefix of an old insert, ensure we set that this is a word
        this.insert(word, cNode.children(), ++index);
    }

    void validateWord(String s){
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("word != null && word != \"\"");
    }

}
