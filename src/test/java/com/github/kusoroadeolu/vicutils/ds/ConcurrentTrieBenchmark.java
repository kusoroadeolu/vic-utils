package com.github.kusoroadeolu.vicutils.ds;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class ConcurrentTrieBenchmark {

    @State(Scope.Benchmark)
    public static class TrieState {
        Trie trie;


        @Setup(Level.Iteration)
        public void setup() {
            trie = new ConcurrentTrie(); //Global lock
        }
    }

    @State(Scope.Benchmark)
    public static class WordGenerator {
        private Random random = ThreadLocalRandom.current();
        private final String[] hotPrefixes = {"aa", "ab", "ac", "ad", "ae"};

        // Evenly distributed - no hot prefixes
        public String generateWord() {
            // Random first char across entire alphabet
            char first = (char)('a' + random.nextInt(26));
            int length = 3 + random.nextInt(8);
            StringBuilder sb = new StringBuilder();
            sb.append(first);
            for (int i = 1; i < length; i++) {
                sb.append((char)('a' + random.nextInt(26)));
            }
            return sb.toString();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(warmups = 2, value = 2)
    @Measurement(iterations = 5)
    @Threads(8)
    public void addAndRead(TrieState state, WordGenerator gen) {
        state.trie.add(gen.generateWord()); // 100% writes, evenly distributed

    }
}

class TrieRunner{
    static void main(String[] args) throws IOException {
        Main.main(args);
    }
}
