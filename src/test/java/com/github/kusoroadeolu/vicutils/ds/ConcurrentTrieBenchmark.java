package com.github.kusoroadeolu.vicutils.ds;

import org.junit.jupiter.api.BeforeEach;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentTrieBenchmark {
    @State(Scope.Benchmark)
    public static class TrieState{
        Trie trie;

        @Setup(Level.Iteration)
        public void setup(){
            trie = new ConcurrentTrie();
        }
    }

    @State(Scope.Thread)
    public static class WordGenerator {
        private Random random = ThreadLocalRandom.current();

        public String generateWord() {
            int length = 3 + random.nextInt(8);
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append((char) ('a' + random.nextInt(26)));
            }
            return sb.toString();
        }
    }

    @Benchmark
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @Fork(warmups = 1, value = 1)
    public void add(TrieState state, WordGenerator gen){
        state.trie.add(gen.generateWord()); // Call it as a method
    }

}

class TrieRunner{
    public static void main(String[] args) throws IOException {
        Main.main(args);
    }
}