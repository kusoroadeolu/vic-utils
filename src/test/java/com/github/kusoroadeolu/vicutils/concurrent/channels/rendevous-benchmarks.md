# A 4P 4C Benchmark Impl


# Unbuffered channel 
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=60962" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.BenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=60962 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannelBenchmarks.send

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 14394.235 ops/s
# Warmup Iteration   2: 17899.285 ops/s
# Warmup Iteration   3: 14627.959 ops/s
Iteration   1: 14460.685 ops/s
Iteration   2: 16120.846 ops/s
Iteration   3: 16630.091 ops/s

# Run progress: 33.33% complete, ETA 00:01:05
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 10203.103 ops/s
# Warmup Iteration   2: 12844.642 ops/s
# Warmup Iteration   3: 14179.384 ops/s
Iteration   1: 13971.511 ops/s
Iteration   2: 16618.372 ops/s
Iteration   3: 15938.852 ops/s

# Run progress: 66.67% complete, ETA 00:00:32
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 11581.842 ops/s
# Warmup Iteration   2: 11670.621 ops/s
# Warmup Iteration   3: 14694.252 ops/s
Iteration   1: 16096.371 ops/s
Iteration   2: 17462.436 ops/s
Iteration   3: 16204.163 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannelBenchmarks.send":
15944.814 ±(99.9%) 1825.834 ops/s [Average]
(min, avg, max) = (13971.511, 15944.814, 17462.436), stdev = 1086.524
CI (99.9%): [14118.981, 17770.648] (assumes normal distribution)


# Run complete. Total time: 00:01:37

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

Benchmark                          Mode  Cnt      Score      Error  Units
UnBufferedChannelBenchmarks.send  thrpt    9  15944.814 ± 1825.834  ops/s

Process finished with exit code 0


# Rendezvous Channel
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=56644" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.BenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=56644 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannelBenchmarks.send

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 20260.554 ops/s
# Warmup Iteration   2: 23515.398 ops/s
# Warmup Iteration   3: 22974.704 ops/s
Iteration   1: 22441.246 ops/s
Iteration   2: 22559.137 ops/s
Iteration   3: 23499.059 ops/s

# Run progress: 33.33% complete, ETA 00:01:03
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 21192.999 ops/s
# Warmup Iteration   2: 22377.363 ops/s
# Warmup Iteration   3: 24121.057 ops/s
Iteration   1: 23291.214 ops/s
Iteration   2: 22885.120 ops/s
Iteration   3: 22639.459 ops/s

# Run progress: 66.67% complete, ETA 00:00:32
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 18908.011 ops/s
# Warmup Iteration   2: 19674.252 ops/s
# Warmup Iteration   3: 20664.183 ops/s
Iteration   1: 23092.206 ops/s
Iteration   2: 23247.031 ops/s
Iteration   3: 24305.413 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannelBenchmarks.send":
23106.654 ±(99.9%) 968.052 ops/s [Average]
(min, avg, max) = (22441.246, 23106.654, 24305.413), stdev = 576.072
CI (99.9%): [22138.602, 24074.706] (assumes normal distribution)


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

Benchmark                          Mode  Cnt      Score     Error  Units
UnBufferedChannelBenchmarks.send  thrpt    9  23106.654 ± 968.052  ops/s

Process finished with exit code 0

# Spin Rendezvous Channel
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=64902" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.BenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=64902 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannelBenchmarks.send

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 6656589.069 ops/s
# Warmup Iteration   2: 6613690.935 ops/s
# Warmup Iteration   3: 6639017.968 ops/s
Iteration   1: 6666334.140 ops/s
Iteration   2: 6411318.924 ops/s
Iteration   3: 6425923.391 ops/s

# Run progress: 33.33% complete, ETA 00:01:02
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 6459432.639 ops/s
# Warmup Iteration   2: 6576118.473 ops/s
# Warmup Iteration   3: 6588417.966 ops/s
Iteration   1: 6567072.845 ops/s
Iteration   2: 6402569.314 ops/s
Iteration   3: 6556176.654 ops/s

# Run progress: 66.67% complete, ETA 00:00:31
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 7827978.789 ops/s
# Warmup Iteration   2: 7927919.783 ops/s
# Warmup Iteration   3: 7940121.296 ops/s
Iteration   1: 8399850.279 ops/s
Iteration   2: 8321665.563 ops/s
Iteration   3: 7498786.040 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannelBenchmarks.send":
7027744.128 ±(99.9%) 1390428.246 ops/s [Average]
(min, avg, max) = (6402569.314, 7027744.128, 8399850.279), stdev = 827421.547
CI (99.9%): [5637315.882, 8418172.374] (assumes normal distribution)


# Run complete. Total time: 00:01:32

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

Benchmark                          Mode  Cnt        Score         Error  Units
UnBufferedChannelBenchmarks.send  thrpt    9  7027744.128 ± 1390428.246  ops/s

Process finished with exit code 0


# Array Blocking Queue 
"C:\Program Files\Java\jdk-25\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=60513" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\eastw\Git Projects\Personal\vic-utils\target\test-classes;C:\Users\eastw\Git Projects\Personal\vic-utils\target\classes;C:\Users\eastw\.m2\repository\org\junit\jupiter\junit-jupiter-api\6.1.0-M1\junit-jupiter-api-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\eastw\.m2\repository\org\junit\platform\junit-platform-commons\6.1.0-M1\junit-platform-commons-6.1.0-M1.jar;C:\Users\eastw\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\eastw\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-core\1.37\jmh-core-1.37.jar;C:\Users\eastw\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\eastw\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;C:\Users\eastw\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.37\jmh-generator-annprocess-1.37.jar" com.github.kusoroadeolu.vicutils.concurrent.channels.BenchmarkRunner
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\Java\jdk-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.2\lib\idea_rt.jar=60513 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 5 s each
# Measurement: 3 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 4 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannelBenchmarks.send

# Run progress: 0.00% complete, ETA 00:01:30
# Fork: 1 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 36156.861 ops/s
# Warmup Iteration   2: 35358.087 ops/s
# Warmup Iteration   3: 36450.577 ops/s
Iteration   1: 36428.374 ops/s
Iteration   2: 37571.219 ops/s
Iteration   3: 36315.631 ops/s

# Run progress: 33.33% complete, ETA 00:01:02
# Fork: 2 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 39148.141 ops/s
# Warmup Iteration   2: 36233.843 ops/s
# Warmup Iteration   3: 37790.232 ops/s
Iteration   1: 35608.914 ops/s
Iteration   2: 31146.416 ops/s
Iteration   3: 30775.873 ops/s

# Run progress: 66.67% complete, ETA 00:00:30
# Fork: 3 of 3
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.openjdk.jmh.util.Utils (file:/C:/Users/eastw/.m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar)
WARNING: Please consider reporting this to the maintainers of class org.openjdk.jmh.util.Utils
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
# Warmup Iteration   1: 38490.890 ops/s
# Warmup Iteration   2: 36622.568 ops/s
# Warmup Iteration   3: 38518.231 ops/s
Iteration   1: 36951.766 ops/s
Iteration   2: 36918.246 ops/s
Iteration   3: 36837.743 ops/s


Result "com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannelBenchmarks.send":
35394.909 ±(99.9%) 4321.579 ops/s [Average]
(min, avg, max) = (30775.873, 35394.909, 37571.219), stdev = 2571.702
CI (99.9%): [31073.330, 39716.488] (assumes normal distribution)


# Run complete. Total time: 00:01:32

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

Benchmark                          Mode  Cnt      Score      Error  Units
UnBufferedChannelBenchmarks.send  thrpt    9  35394.909 ± 4321.579  ops/s

Process finished with exit code 0
