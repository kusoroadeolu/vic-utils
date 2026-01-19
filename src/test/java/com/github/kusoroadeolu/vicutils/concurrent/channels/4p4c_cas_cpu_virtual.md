# 4P 4C Benchmark on my channels implementations to measure CPU efficiency
# Spin Rendezvous
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=62925" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=62925 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      26.06 core-seconds
Producer CPU time:   26.06 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.21 cores (65.2% of system)
Avg producer cores:  5.21 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

5159505.404 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      27.22 core-seconds
Producer CPU time:   27.22 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.44 cores (68.0% of system)
Avg producer cores:  5.44 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

5857139.098 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      31.50 core-seconds
Producer CPU time:   31.50 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      6.30 cores (78.8% of system)
Avg producer cores:  6.30 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

6008667.473 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.20 core-seconds
Producer CPU time:   36.20 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.24 cores (90.5% of system)
Avg producer cores:  7.24 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

6427671.843 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      34.75 core-seconds
Producer CPU time:   34.75 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      6.95 cores (86.9% of system)
Avg producer cores:  6.95 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

6541770.612 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      33.83 core-seconds
Producer CPU time:   33.83 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      6.77 cores (84.6% of system)
Avg producer cores:  6.77 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

6180454.612 ops/s

# Run progress: 33.33% complete, ETA 00:01:07
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      29.52 core-seconds
Producer CPU time:   29.52 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.90 cores (73.8% of system)
Avg producer cores:  5.90 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

5922585.211 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      33.77 core-seconds
Producer CPU time:   33.77 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      6.75 cores (84.4% of system)
Avg producer cores:  6.75 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

6561000.639 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      17.27 core-seconds
Producer CPU time:   17.27 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      3.45 cores (43.2% of system)
Avg producer cores:  3.45 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

3773545.261 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      24.41 core-seconds
Producer CPU time:   24.41 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      4.88 cores (61.0% of system)
Avg producer cores:  4.88 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

5384694.553 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      26.36 core-seconds
Producer CPU time:   26.36 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.27 cores (65.9% of system)
Avg producer cores:  5.27 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

5780880.936 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      29.84 core-seconds
Producer CPU time:   29.84 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.97 cores (74.6% of system)
Avg producer cores:  5.97 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

6084915.940 ops/s

# Run progress: 66.67% complete, ETA 00:00:33
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.88 core-seconds
Producer CPU time:   35.88 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.18 cores (89.7% of system)
Avg producer cores:  7.18 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

6608245.892 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.17 core-seconds
Producer CPU time:   35.17 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.03 cores (87.9% of system)
Avg producer cores:  7.03 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

6481184.233 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.33 core-seconds
Producer CPU time:   36.33 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.27 cores (90.8% of system)
Avg producer cores:  7.27 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

5926340.605 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      34.94 core-seconds
Producer CPU time:   34.94 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      6.99 cores (87.3% of system)
Avg producer cores:  6.99 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

5519933.929 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      34.28 core-seconds
Producer CPU time:   34.28 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      6.86 cores (85.7% of system)
Avg producer cores:  6.86 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

5525344.687 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      29.30 core-seconds
Producer CPU time:   29.30 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.86 cores (73.2% of system)
Avg producer cores:  5.86 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

4759072.577 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
5800526.632 ±(99.9%) 954370.754 ops/s [Average]
(min, avg, max) = (4759072.577, 5800526.632, 6541770.612), stdev = 567930.728
CI (99.9%): [4846155.878, 6754897.386] (assumes normal distribution)


# Run complete. Total time: 00:01:38

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark                               Mode  Cnt        Score        Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  5800526.632 ± 954370.754  ops/s

Process finished with exit code 0


# Rendezvous
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=60686" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=60686 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.42 core-seconds
Producer CPU time:   5.42 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.08 cores (13.6% of system)
Avg producer cores:  1.08 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

19756.065 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.25 core-seconds
Producer CPU time:   5.25 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.05 cores (13.1% of system)
Avg producer cores:  1.05 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

23486.002 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.58 core-seconds
Producer CPU time:   4.58 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.92 cores (11.4% of system)
Avg producer cores:  0.92 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

23506.939 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.89 core-seconds
Producer CPU time:   4.89 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.98 cores (12.2% of system)
Avg producer cores:  0.98 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

22806.855 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.80 core-seconds
Producer CPU time:   4.80 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.96 cores (12.0% of system)
Avg producer cores:  0.96 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

22188.981 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.14 core-seconds
Producer CPU time:   5.14 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.03 cores (12.9% of system)
Avg producer cores:  1.03 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

20705.106 ops/s

# Run progress: 33.33% complete, ETA 00:01:07
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.13 core-seconds
Producer CPU time:   5.13 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.03 cores (12.8% of system)
Avg producer cores:  1.03 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

19326.742 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.75 core-seconds
Producer CPU time:   4.75 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.95 cores (11.9% of system)
Avg producer cores:  0.95 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

21546.152 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.69 core-seconds
Producer CPU time:   4.69 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.94 cores (11.7% of system)
Avg producer cores:  0.94 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

21559.566 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.39 core-seconds
Producer CPU time:   4.39 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.88 cores (11.0% of system)
Avg producer cores:  0.88 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

23637.757 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.80 core-seconds
Producer CPU time:   4.80 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.96 cores (12.0% of system)
Avg producer cores:  0.96 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

22142.147 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.59 core-seconds
Producer CPU time:   4.59 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.92 cores (11.5% of system)
Avg producer cores:  0.92 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

21213.725 ops/s

# Run progress: 66.67% complete, ETA 00:00:33
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.25 core-seconds
Producer CPU time:   5.25 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.05 cores (13.1% of system)
Avg producer cores:  1.05 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

15834.031 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.66 core-seconds
Producer CPU time:   4.66 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.93 cores (11.6% of system)
Avg producer cores:  0.93 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

19122.882 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.64 core-seconds
Producer CPU time:   4.64 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.93 cores (11.6% of system)
Avg producer cores:  0.93 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

20388.901 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.98 core-seconds
Producer CPU time:   4.98 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.00 cores (12.5% of system)
Avg producer cores:  1.00 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

19574.565 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.75 core-seconds
Producer CPU time:   4.75 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.95 cores (11.9% of system)
Avg producer cores:  0.95 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

20522.310 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.86 core-seconds
Producer CPU time:   4.86 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.97 cores (12.1% of system)
Avg producer cores:  0.97 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

20842.213 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
21514.851 ±(99.9%) 2139.885 ops/s [Average]
(min, avg, max) = (19574.565, 21514.851, 23637.757), stdev = 1273.411
CI (99.9%): [19374.966, 23654.736] (assumes normal distribution)


# Run complete. Total time: 00:01:38

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark                               Mode  Cnt      Score      Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  21514.851 ± 2139.885  ops/s

Process finished with exit code 0

# UnBuffered
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=54189" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=54189 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.56 core-seconds
Producer CPU time:   4.56 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.91 cores (11.4% of system)
Avg producer cores:  0.91 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

13139.937 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.72 core-seconds
Producer CPU time:   4.72 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.94 cores (11.8% of system)
Avg producer cores:  0.94 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

13056.227 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.91 core-seconds
Producer CPU time:   4.91 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.98 cores (12.3% of system)
Avg producer cores:  0.98 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

16437.974 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.94 core-seconds
Producer CPU time:   4.94 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.99 cores (12.3% of system)
Avg producer cores:  0.99 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

15041.928 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.89 core-seconds
Producer CPU time:   4.89 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.98 cores (12.2% of system)
Avg producer cores:  0.98 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

15384.956 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.08 core-seconds
Producer CPU time:   5.08 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.02 cores (12.7% of system)
Avg producer cores:  1.02 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

15353.874 ops/s

# Run progress: 33.33% complete, ETA 00:01:09
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.94 core-seconds
Producer CPU time:   4.94 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.99 cores (12.3% of system)
Avg producer cores:  0.99 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

14574.968 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.94 core-seconds
Producer CPU time:   4.94 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.99 cores (12.3% of system)
Avg producer cores:  0.99 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

14156.569 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.39 core-seconds
Producer CPU time:   4.39 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.88 cores (11.0% of system)
Avg producer cores:  0.88 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

13091.606 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.80 core-seconds
Producer CPU time:   5.80 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.16 cores (14.5% of system)
Avg producer cores:  1.16 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

16062.357 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.38 core-seconds
Producer CPU time:   4.38 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.88 cores (10.9% of system)
Avg producer cores:  0.88 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

18805.045 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.61 core-seconds
Producer CPU time:   4.61 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.92 cores (11.5% of system)
Avg producer cores:  0.92 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

14676.059 ops/s

# Run progress: 66.67% complete, ETA 00:00:34
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.67 core-seconds
Producer CPU time:   4.67 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.93 cores (11.7% of system)
Avg producer cores:  0.93 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

14518.433 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.61 core-seconds
Producer CPU time:   4.61 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.92 cores (11.5% of system)
Avg producer cores:  0.92 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

14653.891 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.14 core-seconds
Producer CPU time:   5.14 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.03 cores (12.9% of system)
Avg producer cores:  1.03 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

15574.039 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.58 core-seconds
Producer CPU time:   4.58 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.92 cores (11.4% of system)
Avg producer cores:  0.92 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

14326.643 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.83 core-seconds
Producer CPU time:   5.83 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.17 cores (14.6% of system)
Avg producer cores:  1.17 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

16939.218 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.66 core-seconds
Producer CPU time:   4.66 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      0.93 cores (11.6% of system)
Avg producer cores:  0.93 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

17628.637 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
16024.302 ±(99.9%) 2497.302 ops/s [Average]
(min, avg, max) = (14326.643, 16024.302, 18805.045), stdev = 1486.104
CI (99.9%): [13527.000, 18521.604] (assumes normal distribution)


# Run complete. Total time: 00:01:45

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark                               Mode  Cnt      Score      Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  16024.302 ± 2497.302  ops/s

Process finished with exit code 0

# Array Blocking Queue
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=49684" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=49684 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.58 core-seconds
Producer CPU time:   5.58 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.12 cores (13.9% of system)
Avg producer cores:  1.12 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37956.186 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.19 core-seconds
Producer CPU time:   5.19 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.04 cores (13.0% of system)
Avg producer cores:  1.04 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37393.123 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.27 core-seconds
Producer CPU time:   5.27 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.05 cores (13.2% of system)
Avg producer cores:  1.05 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

36961.680 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.42 core-seconds
Producer CPU time:   5.42 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.08 cores (13.6% of system)
Avg producer cores:  1.08 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37418.322 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.39 core-seconds
Producer CPU time:   5.39 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.08 cores (13.5% of system)
Avg producer cores:  1.08 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37303.745 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.34 core-seconds
Producer CPU time:   5.34 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.07 cores (13.4% of system)
Avg producer cores:  1.07 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37633.597 ops/s

# Run progress: 33.33% complete, ETA 00:01:04
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.73 core-seconds
Producer CPU time:   5.73 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.15 cores (14.3% of system)
Avg producer cores:  1.15 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

38238.141 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.31 core-seconds
Producer CPU time:   5.31 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.06 cores (13.3% of system)
Avg producer cores:  1.06 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37517.593 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.45 core-seconds
Producer CPU time:   5.45 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.09 cores (13.6% of system)
Avg producer cores:  1.09 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37275.701 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.06 core-seconds
Producer CPU time:   5.06 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.01 cores (12.7% of system)
Avg producer cores:  1.01 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37870.544 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.23 core-seconds
Producer CPU time:   5.23 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.05 cores (13.1% of system)
Avg producer cores:  1.05 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

35175.516 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.31 core-seconds
Producer CPU time:   5.31 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.06 cores (13.3% of system)
Avg producer cores:  1.06 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37895.317 ops/s

# Run progress: 66.67% complete, ETA 00:00:32
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.66 core-seconds
Producer CPU time:   5.66 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.13 cores (14.1% of system)
Avg producer cores:  1.13 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37742.574 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.09 core-seconds
Producer CPU time:   5.09 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.02 cores (12.7% of system)
Avg producer cores:  1.02 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

35575.451 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.39 core-seconds
Producer CPU time:   5.39 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.08 cores (13.5% of system)
Avg producer cores:  1.08 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

35710.040 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.34 core-seconds
Producer CPU time:   5.34 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.07 cores (13.4% of system)
Avg producer cores:  1.07 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

36552.409 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.16 core-seconds
Producer CPU time:   5.16 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.03 cores (12.9% of system)
Avg producer cores:  1.03 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

36746.384 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.14 core-seconds
Producer CPU time:   5.14 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      1.03 cores (12.9% of system)
Avg producer cores:  1.03 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

37328.053 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
37102.654 ±(99.9%) 1435.066 ops/s [Average]
(min, avg, max) = (35175.516, 37102.654, 37895.317), stdev = 853.985
CI (99.9%): [35667.588, 38537.720] (assumes normal distribution)


# Run complete. Total time: 00:01:36

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark                               Mode  Cnt      Score      Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  37102.654 ± 1435.066  ops/s

Process finished with exit code 0

# Synchronous Queue
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=57794" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=57794 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      28.17 core-seconds
Producer CPU time:   28.17 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.63 cores (70.4% of system)
Avg producer cores:  5.63 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

3511476.729 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.11 core-seconds
Producer CPU time:   35.11 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.02 cores (87.8% of system)
Avg producer cores:  7.02 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3435880.845 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.33 core-seconds
Producer CPU time:   35.33 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.07 cores (88.3% of system)
Avg producer cores:  7.07 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3265653.171 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.77 core-seconds
Producer CPU time:   35.77 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.15 cores (89.4% of system)
Avg producer cores:  7.15 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3287347.688 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.61 core-seconds
Producer CPU time:   36.61 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.32 cores (91.5% of system)
Avg producer cores:  7.32 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3295334.919 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.58 core-seconds
Producer CPU time:   36.58 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.32 cores (91.4% of system)
Avg producer cores:  7.32 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3292299.716 ops/s

# Run progress: 33.33% complete, ETA 00:01:04
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      28.05 core-seconds
Producer CPU time:   28.05 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.61 cores (70.1% of system)
Avg producer cores:  5.61 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

3439195.499 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.03 core-seconds
Producer CPU time:   36.03 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.21 cores (90.1% of system)
Avg producer cores:  7.21 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3212083.348 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.89 core-seconds
Producer CPU time:   35.89 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.18 cores (89.7% of system)
Avg producer cores:  7.18 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

2971956.188 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.59 core-seconds
Producer CPU time:   35.59 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.12 cores (89.0% of system)
Avg producer cores:  7.12 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

2988582.310 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.69 core-seconds
Producer CPU time:   35.69 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.14 cores (89.2% of system)
Avg producer cores:  7.14 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3008825.086 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.09 core-seconds
Producer CPU time:   36.09 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.22 cores (90.2% of system)
Avg producer cores:  7.22 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3026545.764 ops/s

# Run progress: 66.67% complete, ETA 00:00:32
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      27.33 core-seconds
Producer CPU time:   27.33 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      5.47 cores (68.3% of system)
Avg producer cores:  5.47 cores
Avg consumer cores:  0.00 cores
Threads tracked:     18
===========================================

3603533.760 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.09 core-seconds
Producer CPU time:   35.09 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.02 cores (87.7% of system)
Avg producer cores:  7.02 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3274551.217 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.98 core-seconds
Producer CPU time:   35.98 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.20 cores (90.0% of system)
Avg producer cores:  7.20 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3003851.295 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.17 core-seconds
Producer CPU time:   36.17 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.23 cores (90.4% of system)
Avg producer cores:  7.23 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3022226.192 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.94 core-seconds
Producer CPU time:   35.94 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.19 cores (89.8% of system)
Avg producer cores:  7.19 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3053758.475 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.77 core-seconds
Producer CPU time:   36.77 core-seconds
Consumer CPU time:   0.00 core-seconds
Avg cores used:      7.35 cores (91.9% of system)
Avg producer cores:  7.35 cores
Avg consumer cores:  0.00 cores
Threads tracked:     21
===========================================

3030565.515 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
3111720.629 ±(99.9%) 228662.856 ops/s [Average]
(min, avg, max) = (2988582.310, 3111720.629, 3295334.919), stdev = 136073.598
CI (99.9%): [2883057.773, 3340383.485] (assumes normal distribution)


# Run complete. Total time: 00:01:36

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark                               Mode  Cnt        Score        Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  3111720.629 ± 228662.856  ops/s

Process finished with exit code 0
