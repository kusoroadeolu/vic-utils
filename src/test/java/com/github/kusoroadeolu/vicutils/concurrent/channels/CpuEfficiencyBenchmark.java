package com.github.kusoroadeolu.vicutils.concurrent.channels;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.IterationParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Map;
import java.util.concurrent.*;

@Fork(3)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
public class CpuEfficiencyBenchmark {

    @State(Scope.Benchmark)
    public static class CpuTrackingState {
        ThreadMXBean threadBean;
        Map<Long, Long> threadCpuStart;
        Channel<String> channel;
        //BlockingQueue<String> bq;
        volatile boolean running;

        // Track consumer threads separately
        ConcurrentLinkedQueue<Long> consumerThreadIds;

        @Setup(Level.Trial)
        public void setup() {
            threadBean = ManagementFactory.getThreadMXBean();
            if (!threadBean.isThreadCpuTimeSupported()) {
                throw new RuntimeException("Thread CPU time not supported on this JVM");
            }
            threadBean.setThreadCpuTimeEnabled(true);
            //bq = new ArrayBlockingQueue<>(1);
            channel = new UnBufferedChannel<>();
            channel.make();
            running = true;
            consumerThreadIds = new ConcurrentLinkedQueue<>();

            // Start 4 consumer threads
            for (int i = 0; i < 4; i++) {
                Thread.ofPlatform().start(() -> {
                    consumerThreadIds.add(Thread.currentThread().threadId());
                    while (running) {
                        channel.receive();
                    }
                });
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Setup(Level.Iteration)
        public void beforeIteration() {
            threadCpuStart = new ConcurrentHashMap<>();
            // Capture CPU time for all threads
            for (long tid : threadBean.getAllThreadIds()) {
                long cpuTime = threadBean.getThreadCpuTime(tid);
                if (cpuTime != -1) {  // -1 means thread died or CPU time unavailable
                    threadCpuStart.put(tid, cpuTime);
                }
            }
        }

        @TearDown(Level.Iteration)
        public void afterIteration(BenchmarkParams params, IterationParams iter) {
            long totalCpuNanos = 0;
            long producerCpuNanos = 0;
            long consumerCpuNanos = 0;
            int threadsTracked = 0;

            for (long tid : threadBean.getAllThreadIds()) {
                Long startCpu = threadCpuStart.get(tid);
                if (startCpu != null) {
                    long endCpu = threadBean.getThreadCpuTime(tid);
                    if (endCpu != -1) {
                        long cpuUsed = endCpu - startCpu;
                        totalCpuNanos += cpuUsed;

                        if (consumerThreadIds.contains(tid)) {
                            consumerCpuNanos += cpuUsed;
                        } else {
                            // Assume non-consumer threads are producers (or JMH infrastructure)
                            producerCpuNanos += cpuUsed;
                        }
                        threadsTracked++;
                    }
                }
            }

            double wallClockSeconds = iter.getTime().convertTo(TimeUnit.SECONDS);
            double totalCpuSeconds = totalCpuNanos / 1_000_000_000.0;
            double producerCpuSeconds = producerCpuNanos / 1_000_000_000.0;
            double consumerCpuSeconds = consumerCpuNanos / 1_000_000_000.0;

            double avgCores = totalCpuSeconds / wallClockSeconds;
            double producerCores = producerCpuSeconds / wallClockSeconds;
            double consumerCores = consumerCpuSeconds / wallClockSeconds;

            System.out.println("\n========== CPU Efficiency Report ==========");
            System.out.printf("Wall clock time:     %.2f seconds%n", wallClockSeconds);
            System.out.printf("Total CPU time:      %.2f core-seconds%n", totalCpuSeconds);
            System.out.printf("Producer CPU time:   %.2f core-seconds%n", producerCpuSeconds);
            System.out.printf("Consumer CPU time:   %.2f core-seconds%n", consumerCpuSeconds);
            System.out.printf("Avg cores used:      %.2f cores (%.1f%% of system)%n",
                    avgCores, (avgCores / Runtime.getRuntime().availableProcessors()) * 100);
            System.out.printf("Avg producer cores:  %.2f cores%n", producerCores);
            System.out.printf("Avg consumer cores:  %.2f cores%n", consumerCores);
            System.out.printf("Threads tracked:     %d%n", threadsTracked);
            System.out.println("===========================================\n");
        }

        @TearDown(Level.Trial)
        public void cleanup() {
            running = false;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Benchmark
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void sendThroughput(CpuTrackingState state) throws InterruptedException {
        state.channel.send("x");
    }
}

class CpuBenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(CpuEfficiencyBenchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .build();
        new Runner(opt).run();
    }
}