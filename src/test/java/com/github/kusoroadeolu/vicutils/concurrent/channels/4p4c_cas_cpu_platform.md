# 4P 4C Benchmark on my channels implementations to measure CPU efficiency
# Synchronous Queue
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=55730" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=55730 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
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
Total CPU time:      36.05 core-seconds
Producer CPU time:   18.09 core-seconds
Consumer CPU time:   17.95 core-seconds
Avg cores used:      7.21 cores (90.1% of system)
Avg producer cores:  3.62 cores
Avg consumer cores:  3.59 cores
Threads tracked:     16
===========================================

3415491.387 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.80 core-seconds
Producer CPU time:   17.78 core-seconds
Consumer CPU time:   18.02 core-seconds
Avg cores used:      7.16 cores (89.5% of system)
Avg producer cores:  3.56 cores
Avg consumer cores:  3.60 cores
Threads tracked:     16
===========================================

3483146.086 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.48 core-seconds
Producer CPU time:   18.25 core-seconds
Consumer CPU time:   18.23 core-seconds
Avg cores used:      7.30 cores (91.2% of system)
Avg producer cores:  3.65 cores
Avg consumer cores:  3.65 cores
Threads tracked:     16
===========================================

3316231.790 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.30 core-seconds
Producer CPU time:   18.23 core-seconds
Consumer CPU time:   18.06 core-seconds
Avg cores used:      7.26 cores (90.7% of system)
Avg producer cores:  3.65 cores
Avg consumer cores:  3.61 cores
Threads tracked:     16
===========================================

3328390.760 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.52 core-seconds
Producer CPU time:   18.19 core-seconds
Consumer CPU time:   18.33 core-seconds
Avg cores used:      7.30 cores (91.3% of system)
Avg producer cores:  3.64 cores
Avg consumer cores:  3.67 cores
Threads tracked:     16
===========================================

3268551.773 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.47 core-seconds
Producer CPU time:   18.33 core-seconds
Consumer CPU time:   18.14 core-seconds
Avg cores used:      7.29 cores (91.2% of system)
Avg producer cores:  3.67 cores
Avg consumer cores:  3.63 cores
Threads tracked:     16
===========================================

3318792.805 ops/s

# Run progress: 33.33% complete, ETA 00:01:04
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      30.09 core-seconds
Producer CPU time:   15.17 core-seconds
Consumer CPU time:   14.92 core-seconds
Avg cores used:      6.02 cores (75.2% of system)
Avg producer cores:  3.03 cores
Avg consumer cores:  2.98 cores
Threads tracked:     16
===========================================

3500644.179 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      29.89 core-seconds
Producer CPU time:   15.05 core-seconds
Consumer CPU time:   14.84 core-seconds
Avg cores used:      5.98 cores (74.7% of system)
Avg producer cores:  3.01 cores
Avg consumer cores:  2.97 cores
Threads tracked:     16
===========================================

3108523.555 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      28.47 core-seconds
Producer CPU time:   14.39 core-seconds
Consumer CPU time:   14.08 core-seconds
Avg cores used:      5.69 cores (71.2% of system)
Avg producer cores:  2.88 cores
Avg consumer cores:  2.82 cores
Threads tracked:     16
===========================================

3056952.822 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.59 core-seconds
Producer CPU time:   17.98 core-seconds
Consumer CPU time:   17.61 core-seconds
Avg cores used:      7.12 cores (89.0% of system)
Avg producer cores:  3.60 cores
Avg consumer cores:  3.52 cores
Threads tracked:     16
===========================================

3342521.025 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      33.48 core-seconds
Producer CPU time:   16.64 core-seconds
Consumer CPU time:   16.84 core-seconds
Avg cores used:      6.70 cores (83.7% of system)
Avg producer cores:  3.33 cores
Avg consumer cores:  3.37 cores
Threads tracked:     16
===========================================

3231379.811 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      31.34 core-seconds
Producer CPU time:   15.73 core-seconds
Consumer CPU time:   15.61 core-seconds
Avg cores used:      6.27 cores (78.4% of system)
Avg producer cores:  3.15 cores
Avg consumer cores:  3.12 cores
Threads tracked:     16
===========================================

3192379.354 ops/s

# Run progress: 66.67% complete, ETA 00:00:32
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.67 core-seconds
Producer CPU time:   17.98 core-seconds
Consumer CPU time:   17.69 core-seconds
Avg cores used:      7.13 cores (89.2% of system)
Avg producer cores:  3.60 cores
Avg consumer cores:  3.54 cores
Threads tracked:     16
===========================================

3399197.678 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      29.08 core-seconds
Producer CPU time:   14.39 core-seconds
Consumer CPU time:   14.69 core-seconds
Avg cores used:      5.82 cores (72.7% of system)
Avg producer cores:  2.88 cores
Avg consumer cores:  2.94 cores
Threads tracked:     16
===========================================

3144756.205 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      27.58 core-seconds
Producer CPU time:   13.86 core-seconds
Consumer CPU time:   13.72 core-seconds
Avg cores used:      5.52 cores (68.9% of system)
Avg producer cores:  2.77 cores
Avg consumer cores:  2.74 cores
Threads tracked:     16
===========================================

2957457.977 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      33.89 core-seconds
Producer CPU time:   16.70 core-seconds
Consumer CPU time:   17.19 core-seconds
Avg cores used:      6.78 cores (84.7% of system)
Avg producer cores:  3.34 cores
Avg consumer cores:  3.44 cores
Threads tracked:     16
===========================================

3201283.148 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      22.59 core-seconds
Producer CPU time:   11.34 core-seconds
Consumer CPU time:   11.25 core-seconds
Avg cores used:      4.52 cores (56.5% of system)
Avg producer cores:  2.27 cores
Avg consumer cores:  2.25 cores
Threads tracked:     16
===========================================

2780677.697 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      31.22 core-seconds
Producer CPU time:   15.72 core-seconds
Consumer CPU time:   15.50 core-seconds
Avg cores used:      6.24 cores (78.0% of system)
Avg producer cores:  3.14 cores
Avg consumer cores:  3.10 cores
Threads tracked:     16
===========================================

3424930.563 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
3232100.771 ±(99.9%) 310882.055 ops/s [Average]
(min, avg, max) = (2780677.697, 3232100.771, 3424930.563), stdev = 185000.924
CI (99.9%): [2921218.715, 3542982.826] (assumes normal distribution)


# Run complete. Total time: 00:01:36

Benchmark                               Mode  Cnt        Score        Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  3232100.771 ± 310882.055  ops/s



# Array Blocking Queue
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=50239" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=50239 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
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
Total CPU time:      3.61 core-seconds
Producer CPU time:   1.80 core-seconds
Consumer CPU time:   1.81 core-seconds
Avg cores used:      0.72 cores (9.0% of system)
Avg producer cores:  0.36 cores
Avg consumer cores:  0.36 cores
Threads tracked:     16
===========================================

34081.392 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.53 core-seconds
Producer CPU time:   1.81 core-seconds
Consumer CPU time:   1.72 core-seconds
Avg cores used:      0.71 cores (8.8% of system)
Avg producer cores:  0.36 cores
Avg consumer cores:  0.34 cores
Threads tracked:     16
===========================================

35857.809 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.70 core-seconds
Producer CPU time:   1.83 core-seconds
Consumer CPU time:   1.88 core-seconds
Avg cores used:      0.74 cores (9.3% of system)
Avg producer cores:  0.37 cores
Avg consumer cores:  0.38 cores
Threads tracked:     16
===========================================

38787.063 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.70 core-seconds
Producer CPU time:   1.88 core-seconds
Consumer CPU time:   1.83 core-seconds
Avg cores used:      0.74 cores (9.3% of system)
Avg producer cores:  0.38 cores
Avg consumer cores:  0.37 cores
Threads tracked:     16
===========================================

39224.737 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.83 core-seconds
Producer CPU time:   1.84 core-seconds
Consumer CPU time:   1.98 core-seconds
Avg cores used:      0.77 cores (9.6% of system)
Avg producer cores:  0.37 cores
Avg consumer cores:  0.40 cores
Threads tracked:     16
===========================================

39332.842 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.70 core-seconds
Producer CPU time:   1.69 core-seconds
Consumer CPU time:   2.02 core-seconds
Avg cores used:      0.74 cores (9.3% of system)
Avg producer cores:  0.34 cores
Avg consumer cores:  0.40 cores
Threads tracked:     16
===========================================

38202.234 ops/s

# Run progress: 33.33% complete, ETA 00:01:04
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.66 core-seconds
Producer CPU time:   1.89 core-seconds
Consumer CPU time:   1.77 core-seconds
Avg cores used:      0.73 cores (9.1% of system)
Avg producer cores:  0.38 cores
Avg consumer cores:  0.35 cores
Threads tracked:     16
===========================================

38331.579 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.75 core-seconds
Producer CPU time:   1.91 core-seconds
Consumer CPU time:   1.84 core-seconds
Avg cores used:      0.75 cores (9.4% of system)
Avg producer cores:  0.38 cores
Avg consumer cores:  0.37 cores
Threads tracked:     16
===========================================

37861.723 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.33 core-seconds
Producer CPU time:   1.69 core-seconds
Consumer CPU time:   1.64 core-seconds
Avg cores used:      0.67 cores (8.3% of system)
Avg producer cores:  0.34 cores
Avg consumer cores:  0.33 cores
Threads tracked:     16
===========================================

38708.905 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.64 core-seconds
Producer CPU time:   1.72 core-seconds
Consumer CPU time:   1.92 core-seconds
Avg cores used:      0.73 cores (9.1% of system)
Avg producer cores:  0.34 cores
Avg consumer cores:  0.38 cores
Threads tracked:     16
===========================================

38449.128 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.77 core-seconds
Producer CPU time:   1.86 core-seconds
Consumer CPU time:   1.91 core-seconds
Avg cores used:      0.75 cores (9.4% of system)
Avg producer cores:  0.37 cores
Avg consumer cores:  0.38 cores
Threads tracked:     16
===========================================

36122.193 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.80 core-seconds
Producer CPU time:   1.94 core-seconds
Consumer CPU time:   1.86 core-seconds
Avg cores used:      0.76 cores (9.5% of system)
Avg producer cores:  0.39 cores
Avg consumer cores:  0.37 cores
Threads tracked:     16
===========================================

38782.304 ops/s

# Run progress: 66.67% complete, ETA 00:00:32
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.63 core-seconds
Producer CPU time:   1.84 core-seconds
Consumer CPU time:   1.78 core-seconds
Avg cores used:      0.73 cores (9.1% of system)
Avg producer cores:  0.37 cores
Avg consumer cores:  0.36 cores
Threads tracked:     16
===========================================

39092.064 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.56 core-seconds
Producer CPU time:   1.86 core-seconds
Consumer CPU time:   1.70 core-seconds
Avg cores used:      0.71 cores (8.9% of system)
Avg producer cores:  0.37 cores
Avg consumer cores:  0.34 cores
Threads tracked:     16
===========================================

37830.930 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.58 core-seconds
Producer CPU time:   1.94 core-seconds
Consumer CPU time:   1.64 core-seconds
Avg cores used:      0.72 cores (8.9% of system)
Avg producer cores:  0.39 cores
Avg consumer cores:  0.33 cores
Threads tracked:     16
===========================================

38049.879 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.52 core-seconds
Producer CPU time:   1.78 core-seconds
Consumer CPU time:   1.73 core-seconds
Avg cores used:      0.70 cores (8.8% of system)
Avg producer cores:  0.36 cores
Avg consumer cores:  0.35 cores
Threads tracked:     16
===========================================

39000.846 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.69 core-seconds
Producer CPU time:   2.25 core-seconds
Consumer CPU time:   1.44 core-seconds
Avg cores used:      0.74 cores (9.2% of system)
Avg producer cores:  0.45 cores
Avg consumer cores:  0.29 cores
Threads tracked:     16
===========================================

39560.499 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.59 core-seconds
Producer CPU time:   1.78 core-seconds
Consumer CPU time:   1.81 core-seconds
Avg cores used:      0.72 cores (9.0% of system)
Avg producer cores:  0.36 cores
Avg consumer cores:  0.36 cores
Threads tracked:     16
===========================================

36046.260 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
38302.338 ±(99.9%) 2230.022 ops/s [Average]
(min, avg, max) = (36046.260, 38302.338, 39560.499), stdev = 1327.050
CI (99.9%): [36072.316, 40532.360] (assumes normal distribution)


# Run complete. Total time: 00:01:36

Benchmark                               Mode  Cnt      Score      Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  38302.338 ± 2230.022  ops/s


# Unbuffered Channel
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=63045" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=63045 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
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
Total CPU time:      3.75 core-seconds
Producer CPU time:   2.61 core-seconds
Consumer CPU time:   1.14 core-seconds
Avg cores used:      0.75 cores (9.4% of system)
Avg producer cores:  0.52 cores
Avg consumer cores:  0.23 cores
Threads tracked:     16
===========================================

21875.631 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.61 core-seconds
Producer CPU time:   2.08 core-seconds
Consumer CPU time:   1.53 core-seconds
Avg cores used:      0.72 cores (9.0% of system)
Avg producer cores:  0.42 cores
Avg consumer cores:  0.31 cores
Threads tracked:     16
===========================================

27028.534 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.56 core-seconds
Producer CPU time:   2.34 core-seconds
Consumer CPU time:   1.22 core-seconds
Avg cores used:      0.71 cores (8.9% of system)
Avg producer cores:  0.47 cores
Avg consumer cores:  0.24 cores
Threads tracked:     16
===========================================

23732.541 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.66 core-seconds
Producer CPU time:   2.34 core-seconds
Consumer CPU time:   1.31 core-seconds
Avg cores used:      0.73 cores (9.1% of system)
Avg producer cores:  0.47 cores
Avg consumer cores:  0.26 cores
Threads tracked:     16
===========================================

27288.597 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.45 core-seconds
Producer CPU time:   2.38 core-seconds
Consumer CPU time:   1.08 core-seconds
Avg cores used:      0.69 cores (8.6% of system)
Avg producer cores:  0.48 cores
Avg consumer cores:  0.22 cores
Threads tracked:     16
===========================================

28417.361 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.80 core-seconds
Producer CPU time:   2.59 core-seconds
Consumer CPU time:   1.20 core-seconds
Avg cores used:      0.76 cores (9.5% of system)
Avg producer cores:  0.52 cores
Avg consumer cores:  0.24 cores
Threads tracked:     16
===========================================

24258.229 ops/s

# Run progress: 33.33% complete, ETA 00:01:05
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.75 core-seconds
Producer CPU time:   2.27 core-seconds
Consumer CPU time:   1.48 core-seconds
Avg cores used:      0.75 cores (9.4% of system)
Avg producer cores:  0.45 cores
Avg consumer cores:  0.30 cores
Threads tracked:     16
===========================================

25322.527 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.63 core-seconds
Producer CPU time:   2.42 core-seconds
Consumer CPU time:   1.20 core-seconds
Avg cores used:      0.73 cores (9.1% of system)
Avg producer cores:  0.48 cores
Avg consumer cores:  0.24 cores
Threads tracked:     16
===========================================

24572.096 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      5.22 core-seconds
Producer CPU time:   3.88 core-seconds
Consumer CPU time:   1.34 core-seconds
Avg cores used:      1.04 cores (13.0% of system)
Avg producer cores:  0.78 cores
Avg consumer cores:  0.27 cores
Threads tracked:     16
===========================================

19274.901 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.30 core-seconds
Producer CPU time:   2.25 core-seconds
Consumer CPU time:   1.05 core-seconds
Avg cores used:      0.66 cores (8.2% of system)
Avg producer cores:  0.45 cores
Avg consumer cores:  0.21 cores
Threads tracked:     16
===========================================

25746.103 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.84 core-seconds
Producer CPU time:   3.27 core-seconds
Consumer CPU time:   1.58 core-seconds
Avg cores used:      0.97 cores (12.1% of system)
Avg producer cores:  0.65 cores
Avg consumer cores:  0.32 cores
Threads tracked:     16
===========================================

29029.261 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.53 core-seconds
Producer CPU time:   2.55 core-seconds
Consumer CPU time:   0.98 core-seconds
Avg cores used:      0.71 cores (8.8% of system)
Avg producer cores:  0.51 cores
Avg consumer cores:  0.20 cores
Threads tracked:     16
===========================================

21065.597 ops/s

# Run progress: 66.67% complete, ETA 00:00:34
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.92 core-seconds
Producer CPU time:   2.73 core-seconds
Consumer CPU time:   1.19 core-seconds
Avg cores used:      0.78 cores (9.8% of system)
Avg producer cores:  0.55 cores
Avg consumer cores:  0.24 cores
Threads tracked:     16
===========================================

25340.534 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.20 core-seconds
Producer CPU time:   2.88 core-seconds
Consumer CPU time:   1.33 core-seconds
Avg cores used:      0.84 cores (10.5% of system)
Avg producer cores:  0.58 cores
Avg consumer cores:  0.27 cores
Threads tracked:     16
===========================================

26000.698 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.00 core-seconds
Producer CPU time:   2.78 core-seconds
Consumer CPU time:   1.22 core-seconds
Avg cores used:      0.80 cores (10.0% of system)
Avg producer cores:  0.56 cores
Avg consumer cores:  0.24 cores
Threads tracked:     16
===========================================

22266.545 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.75 core-seconds
Producer CPU time:   2.63 core-seconds
Consumer CPU time:   1.13 core-seconds
Avg cores used:      0.75 cores (9.4% of system)
Avg producer cores:  0.53 cores
Avg consumer cores:  0.23 cores
Threads tracked:     16
===========================================

19976.689 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.53 core-seconds
Producer CPU time:   2.58 core-seconds
Consumer CPU time:   0.95 core-seconds
Avg cores used:      0.71 cores (8.8% of system)
Avg producer cores:  0.52 cores
Avg consumer cores:  0.19 cores
Threads tracked:     16
===========================================

18847.786 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.95 core-seconds
Producer CPU time:   2.45 core-seconds
Consumer CPU time:   1.50 core-seconds
Avg cores used:      0.79 cores (9.9% of system)
Avg producer cores:  0.49 cores
Avg consumer cores:  0.30 cores
Threads tracked:     16
===========================================

24547.063 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
24352.965 ±(99.9%) 6210.170 ops/s [Average]
(min, avg, max) = (18847.786, 24352.965, 29029.261), stdev = 3695.573
CI (99.9%): [18142.795, 30563.135] (assumes normal distribution)


# Run complete. Total time: 00:01:42



Benchmark                               Mode  Cnt      Score      Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  24352.965 ± 6210.170  ops/s


# Rendezvous Channel
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=63064 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
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
Total CPU time:      3.89 core-seconds
Producer CPU time:   2.48 core-seconds
Consumer CPU time:   1.41 core-seconds
Avg cores used:      0.78 cores (9.7% of system)
Avg producer cores:  0.50 cores
Avg consumer cores:  0.28 cores
Threads tracked:     16
===========================================

27689.858 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.77 core-seconds
Producer CPU time:   2.52 core-seconds
Consumer CPU time:   1.25 core-seconds
Avg cores used:      0.75 cores (9.4% of system)
Avg producer cores:  0.50 cores
Avg consumer cores:  0.25 cores
Threads tracked:     16
===========================================

27082.056 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.83 core-seconds
Producer CPU time:   2.56 core-seconds
Consumer CPU time:   1.27 core-seconds
Avg cores used:      0.77 cores (9.6% of system)
Avg producer cores:  0.51 cores
Avg consumer cores:  0.25 cores
Threads tracked:     16
===========================================

26628.667 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.19 core-seconds
Producer CPU time:   2.63 core-seconds
Consumer CPU time:   1.56 core-seconds
Avg cores used:      0.84 cores (10.5% of system)
Avg producer cores:  0.53 cores
Avg consumer cores:  0.31 cores
Threads tracked:     16
===========================================

28862.981 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.98 core-seconds
Producer CPU time:   2.39 core-seconds
Consumer CPU time:   1.59 core-seconds
Avg cores used:      0.80 cores (10.0% of system)
Avg producer cores:  0.48 cores
Avg consumer cores:  0.32 cores
Threads tracked:     16
===========================================

32511.038 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.78 core-seconds
Producer CPU time:   2.25 core-seconds
Consumer CPU time:   1.53 core-seconds
Avg cores used:      0.76 cores (9.5% of system)
Avg producer cores:  0.45 cores
Avg consumer cores:  0.31 cores
Threads tracked:     16
===========================================

31032.703 ops/s

# Run progress: 33.33% complete, ETA 00:01:06
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.86 core-seconds
Producer CPU time:   2.33 core-seconds
Consumer CPU time:   1.53 core-seconds
Avg cores used:      0.77 cores (9.6% of system)
Avg producer cores:  0.47 cores
Avg consumer cores:  0.31 cores
Threads tracked:     16
===========================================

26851.028 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.84 core-seconds
Producer CPU time:   2.67 core-seconds
Consumer CPU time:   1.17 core-seconds
Avg cores used:      0.77 cores (9.6% of system)
Avg producer cores:  0.53 cores
Avg consumer cores:  0.23 cores
Threads tracked:     16
===========================================

29181.468 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.78 core-seconds
Producer CPU time:   2.58 core-seconds
Consumer CPU time:   1.20 core-seconds
Avg cores used:      0.76 cores (9.5% of system)
Avg producer cores:  0.52 cores
Avg consumer cores:  0.24 cores
Threads tracked:     16
===========================================

26721.731 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.75 core-seconds
Producer CPU time:   2.08 core-seconds
Consumer CPU time:   1.67 core-seconds
Avg cores used:      0.75 cores (9.4% of system)
Avg producer cores:  0.42 cores
Avg consumer cores:  0.33 cores
Threads tracked:     16
===========================================

27487.513 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.88 core-seconds
Producer CPU time:   2.42 core-seconds
Consumer CPU time:   1.45 core-seconds
Avg cores used:      0.78 cores (9.7% of system)
Avg producer cores:  0.48 cores
Avg consumer cores:  0.29 cores
Threads tracked:     16
===========================================

30158.508 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.86 core-seconds
Producer CPU time:   2.63 core-seconds
Consumer CPU time:   1.23 core-seconds
Avg cores used:      0.77 cores (9.6% of system)
Avg producer cores:  0.53 cores
Avg consumer cores:  0.25 cores
Threads tracked:     16
===========================================

28430.492 ops/s

# Run progress: 66.67% complete, ETA 00:00:32
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.09 core-seconds
Producer CPU time:   2.86 core-seconds
Consumer CPU time:   1.23 core-seconds
Avg cores used:      0.82 cores (10.2% of system)
Avg producer cores:  0.57 cores
Avg consumer cores:  0.25 cores
Threads tracked:     16
===========================================

28159.847 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      4.14 core-seconds
Producer CPU time:   2.69 core-seconds
Consumer CPU time:   1.45 core-seconds
Avg cores used:      0.83 cores (10.4% of system)
Avg producer cores:  0.54 cores
Avg consumer cores:  0.29 cores
Threads tracked:     16
===========================================

27660.341 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.75 core-seconds
Producer CPU time:   2.19 core-seconds
Consumer CPU time:   1.56 core-seconds
Avg cores used:      0.75 cores (9.4% of system)
Avg producer cores:  0.44 cores
Avg consumer cores:  0.31 cores
Threads tracked:     16
===========================================

29058.961 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.64 core-seconds
Producer CPU time:   2.27 core-seconds
Consumer CPU time:   1.38 core-seconds
Avg cores used:      0.73 cores (9.1% of system)
Avg producer cores:  0.45 cores
Avg consumer cores:  0.28 cores
Threads tracked:     16
===========================================

27531.985 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.64 core-seconds
Producer CPU time:   2.30 core-seconds
Consumer CPU time:   1.34 core-seconds
Avg cores used:      0.73 cores (9.1% of system)
Avg producer cores:  0.46 cores
Avg consumer cores:  0.27 cores
Threads tracked:     16
===========================================

27456.711 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      3.84 core-seconds
Producer CPU time:   2.55 core-seconds
Consumer CPU time:   1.30 core-seconds
Avg cores used:      0.77 cores (9.6% of system)
Avg producer cores:  0.51 cores
Avg consumer cores:  0.26 cores
Threads tracked:     16
===========================================

29244.288 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
29190.691 ±(99.9%) 2952.822 ops/s [Average]
(min, avg, max) = (27456.711, 29190.691, 32511.038), stdev = 1757.177
CI (99.9%): [26237.869, 32143.512] (assumes normal distribution)


# Run complete. Total time: 00:01:38

Benchmark                               Mode  Cnt      Score      Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  29190.691 ± 2952.822  ops/s

# Spin Rendezvous Channel
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=56499" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.CpuBenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=56499 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
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
Total CPU time:      36.73 core-seconds
Producer CPU time:   18.44 core-seconds
Consumer CPU time:   18.30 core-seconds
Avg cores used:      7.35 cores (91.8% of system)
Avg producer cores:  3.69 cores
Avg consumer cores:  3.66 cores
Threads tracked:     16
===========================================

6619172.190 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.58 core-seconds
Producer CPU time:   18.39 core-seconds
Consumer CPU time:   18.19 core-seconds
Avg cores used:      7.32 cores (91.4% of system)
Avg producer cores:  3.68 cores
Avg consumer cores:  3.64 cores
Threads tracked:     16
===========================================

6916953.295 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      37.06 core-seconds
Producer CPU time:   18.59 core-seconds
Consumer CPU time:   18.47 core-seconds
Avg cores used:      7.41 cores (92.7% of system)
Avg producer cores:  3.72 cores
Avg consumer cores:  3.69 cores
Threads tracked:     16
===========================================

6289843.691 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.33 core-seconds
Producer CPU time:   18.11 core-seconds
Consumer CPU time:   18.22 core-seconds
Avg cores used:      7.27 cores (90.8% of system)
Avg producer cores:  3.62 cores
Avg consumer cores:  3.64 cores
Threads tracked:     16
===========================================

6203222.047 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      37.34 core-seconds
Producer CPU time:   18.72 core-seconds
Consumer CPU time:   18.63 core-seconds
Avg cores used:      7.47 cores (93.4% of system)
Avg producer cores:  3.74 cores
Avg consumer cores:  3.73 cores
Threads tracked:     16
===========================================

6247556.703 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      37.14 core-seconds
Producer CPU time:   18.59 core-seconds
Consumer CPU time:   18.55 core-seconds
Avg cores used:      7.43 cores (92.9% of system)
Avg producer cores:  3.72 cores
Avg consumer cores:  3.71 cores
Threads tracked:     16
===========================================

6255996.134 ops/s

# Run progress: 33.33% complete, ETA 00:01:04
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.30 core-seconds
Producer CPU time:   17.59 core-seconds
Consumer CPU time:   17.70 core-seconds
Avg cores used:      7.06 cores (88.2% of system)
Avg producer cores:  3.52 cores
Avg consumer cores:  3.54 cores
Threads tracked:     16
===========================================

6679541.829 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.41 core-seconds
Producer CPU time:   17.73 core-seconds
Consumer CPU time:   17.67 core-seconds
Avg cores used:      7.08 cores (88.5% of system)
Avg producer cores:  3.55 cores
Avg consumer cores:  3.53 cores
Threads tracked:     16
===========================================

7097178.508 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.73 core-seconds
Producer CPU time:   17.91 core-seconds
Consumer CPU time:   17.83 core-seconds
Avg cores used:      7.15 cores (89.3% of system)
Avg producer cores:  3.58 cores
Avg consumer cores:  3.57 cores
Threads tracked:     16
===========================================

6426189.768 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.73 core-seconds
Producer CPU time:   17.92 core-seconds
Consumer CPU time:   17.81 core-seconds
Avg cores used:      7.15 cores (89.3% of system)
Avg producer cores:  3.58 cores
Avg consumer cores:  3.56 cores
Threads tracked:     16
===========================================

6403246.337 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.22 core-seconds
Producer CPU time:   18.20 core-seconds
Consumer CPU time:   18.02 core-seconds
Avg cores used:      7.24 cores (90.5% of system)
Avg producer cores:  3.64 cores
Avg consumer cores:  3.60 cores
Threads tracked:     16
===========================================

6466351.540 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.64 core-seconds
Producer CPU time:   18.33 core-seconds
Consumer CPU time:   18.31 core-seconds
Avg cores used:      7.33 cores (91.6% of system)
Avg producer cores:  3.67 cores
Avg consumer cores:  3.66 cores
Threads tracked:     16
===========================================

6540852.546 ops/s

# Run progress: 66.67% complete, ETA 00:00:32
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      33.94 core-seconds
Producer CPU time:   17.14 core-seconds
Consumer CPU time:   16.80 core-seconds
Avg cores used:      6.79 cores (84.8% of system)
Avg producer cores:  3.43 cores
Avg consumer cores:  3.36 cores
Threads tracked:     16
===========================================

6549353.087 ops/s
# Warmup Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.47 core-seconds
Producer CPU time:   18.11 core-seconds
Consumer CPU time:   17.36 core-seconds
Avg cores used:      7.09 cores (88.7% of system)
Avg producer cores:  3.62 cores
Avg consumer cores:  3.47 cores
Threads tracked:     16
===========================================

6830418.052 ops/s
# Warmup Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.34 core-seconds
Producer CPU time:   17.72 core-seconds
Consumer CPU time:   17.63 core-seconds
Avg cores used:      7.07 cores (88.4% of system)
Avg producer cores:  3.54 cores
Avg consumer cores:  3.53 cores
Threads tracked:     16
===========================================

6443110.608 ops/s
Iteration   1:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      36.19 core-seconds
Producer CPU time:   18.34 core-seconds
Consumer CPU time:   17.84 core-seconds
Avg cores used:      7.24 cores (90.5% of system)
Avg producer cores:  3.67 cores
Avg consumer cores:  3.57 cores
Threads tracked:     16
===========================================

6750959.211 ops/s
Iteration   2:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.05 core-seconds
Producer CPU time:   17.73 core-seconds
Consumer CPU time:   17.31 core-seconds
Avg cores used:      7.01 cores (87.6% of system)
Avg producer cores:  3.55 cores
Avg consumer cores:  3.46 cores
Threads tracked:     16
===========================================

6627473.198 ops/s
Iteration   3:
========== CPU Efficiency Report ==========
Wall clock time:     5.00 seconds
Total CPU time:      35.52 core-seconds
Producer CPU time:   17.59 core-seconds
Consumer CPU time:   17.92 core-seconds
Avg cores used:      7.10 cores (88.8% of system)
Avg producer cores:  3.52 cores
Avg consumer cores:  3.58 cores
Threads tracked:     16
===========================================

6645261.406 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.CpuEfficiencyBenchmark.sendThroughput":
6460102.125 ±(99.9%) 330532.095 ops/s [Average]
(min, avg, max) = (6203222.047, 6460102.125, 6750959.211), stdev = 196694.348
CI (99.9%): [6129570.029, 6790634.220] (assumes normal distribution)


# Run complete. Total time: 00:01:36

Benchmark                               Mode  Cnt        Score        Error  Units
CpuEfficiencyBenchmark.sendThroughput  thrpt    9  6460102.125 ± 330532.095  ops/s


