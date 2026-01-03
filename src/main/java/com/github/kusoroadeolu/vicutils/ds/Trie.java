package com.github.kusoroadeolu.vicutils.ds;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface Trie {
    boolean add(String word);

    boolean remove(String word);

    List<String> startsWith(String prefix);

    boolean containsPrefix(String prefix);

    boolean containsExact(String word);

    int size();

    Trie copy();

    class Node {
        private final char c;
        private final Map<Character, Node> children;
        private boolean isWordEnd;

        public Node(char c, Map<Character, Node> children, boolean isWordEnd) {
            this.c = c;
            this.children = children;
            this.isWordEnd = isWordEnd;
        }



        // Getters and Setters
        public char c() { return c; }
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
}
