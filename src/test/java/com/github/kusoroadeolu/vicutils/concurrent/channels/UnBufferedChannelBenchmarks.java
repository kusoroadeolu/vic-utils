package com.github.kusoroadeolu.vicutils.concurrent.channels;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Fork(3)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
public class UnBufferedChannelBenchmarks {

    @State(Scope.Benchmark)
    public static class ChannelState {
        ArrayBlockingQueue<String> abq;
        Channel<String> channel;

        @Setup(Level.Trial)
        public void setup() {
            abq = new ArrayBlockingQueue<>(1);
//            channel = new SpinRendezvousChannel<>();
//            channel.make();
            // Start consumer threads
            for (int i = 0; i < 4; i++) {
                Thread.startVirtualThread(() -> {
                    while (!Thread.interrupted()) {
                        try {
                            abq.take();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
    }

    @Benchmark
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void send(ChannelState state) throws InterruptedException {
        state.abq.put("x");
    }
}


class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(UnBufferedChannelBenchmarks.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}