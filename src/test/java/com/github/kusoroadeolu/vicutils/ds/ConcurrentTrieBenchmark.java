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
            trie = new SynchronizedTrie(); //Global lock
        }
    }

    @State(Scope.Benchmark)
    public static class WordGenerator {
        private Random random = ThreadLocalRandom.current();
        private final String[] hotPrefixes = {"aa", "ab", "ac", "ad", "ae"};

        // 70% chance to pick a hot prefix
        public String generateWord() {
            String prefix;
            if (random.nextInt(100) < 70) {
                prefix = hotPrefixes[random.nextInt(hotPrefixes.length)];
            } else {
                prefix = "" + (char)('a' + random.nextInt(26)) + (char)('a' + random.nextInt(26));
            }

            int length = 3 + random.nextInt(8);
            StringBuilder sb = new StringBuilder(prefix);
            for (int i = prefix.length(); i < length; i++) {
                sb.append((char)('a' + random.nextInt(26)));
            }
            return sb.toString();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    //@OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(warmups = 1, value = 1)
    @Measurement(iterations = 5)
    @Threads(4)
    public void addAndRead(TrieState state, WordGenerator gen) {
        String word = gen.generateWord();

        if (ThreadLocalRandom.current().nextInt(100) < 30) {
            state.trie.add(word); // 30% writes
        } else {
            state.trie.containsExact(word); // 70% reads
        }
    }
}

class TrieRunner{
    static void main(String[] args) throws IOException {
        Main.main(args);
    }
}
