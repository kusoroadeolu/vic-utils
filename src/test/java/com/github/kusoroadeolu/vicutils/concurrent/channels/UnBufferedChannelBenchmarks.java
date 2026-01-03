package com.github.kusoroadeolu.vicutils.concurrent.channels;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ArrayBlockingQueue;

public class UnBufferedChannelBenchmarks {

    @State(Scope.Benchmark)
    public static class ChannelState {
        Channel<String> channel = new UnBufferedChannel<>();
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        @Setup
        public void setup() {
            channel.make();;
            for (int i = 0; i < 4; i++){
                Thread.startVirtualThread(() -> {
                    while (true){
                        channel.receive();
                    }
                });
            }

        }
    }

    @Benchmark
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @Fork(warmups = 0, value = 0)
    public void send(ChannelState state) throws InterruptedException {
        state.channel.send("msg");
    }

}

class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
       Main.main(args);
    }
}