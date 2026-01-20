# CPU Efficiency Analysis - Virtual Thread Consumers (4P/4C)

This document analyzes CPU efficiency benchmarks for channel implementations under a **4 producer (platform threads) / 4 consumer (virtual threads)** configuration.

---

## Benchmark Setup

- **JMH version:** 1.37
- **JVM:** Java 25, HotSpot 64-Bit Server VM (25+37-LTS-3491)
- **Benchmark mode:** Throughput (ops/s)
- **Warmup:** 3 iterations × 5s each
- **Measurement:** 3 iterations × 5s each
- **Forks:** 3
- **Thread configuration:** 4 producers (platform threads) / 4 consumers (virtual threads)
- **CPU tracking:** Measured via ThreadMXBean for producer and consumer threads separately

---

## Results Summary

| Implementation | Throughput (ops/s) | Total CPU Usage | Producer CPU | Consumer CPU | Avg Cores Used |
|----------------|-------------------|-----------------|--------------|--------------|----------------|
| **SpinRendezvousChannel** | 5,800,527 ± 954,371 | ~24-36 core-seconds | ~24-36 core-seconds | 0.00 | 5-7 cores (61-90%) |
| **SynchronousQueue** | 3,111,721 ± 228,663 | ~35-37 core-seconds | ~35-37 core-seconds | 0.00 | ~7 cores (89-91%) |
| **ArrayBlockingQueue** | 37,103 ± 1,435 | ~5.1-5.7 core-seconds | ~5.1-5.7 core-seconds | 0.00 | ~1.0-1.1 cores (13-14%) |
| **RendezvousChannel** | 21,515 ± 2,140 | ~4.4-5.4 core-seconds | ~4.4-5.4 core-seconds | 0.00 | ~0.9-1.1 cores (11-13%) |
| **UnBufferedChannel** | 16,024 ± 2,497 | ~4.4-5.8 core-seconds | ~4.4-5.8 core-seconds | 0.00 | ~0.9-1.2 cores (11-15%) |

---

## What Virtual Thread Consumers Reveal

### The Zero Consumer CPU Phenomenon

**All five implementations show zero consumer CPU usage** despite having 4 active consumer threads:
- SpinRendezvousChannel
- SynchronousQueue
- RendezvousChannel
- UnBufferedChannel
- ArrayBlockingQueue

**What this tells us:** This is a ThreadMXBean accounting limitation. Virtual thread CPU time, when mounted on carrier threads, is not properly attributed back to the virtual thread ID when queried via `ThreadMXBean.getThreadCpuTime()`. The CPU time is either attributed to the carrier thread pool or lost in accounting entirely.

**Key insight:** Platform thread consumers (shown in the comparison document) reveal the true CPU cost - the zero reading here is a measurement artifact, not a fundamental efficiency difference. When we switch to platform thread consumers, the "hidden" CPU usage becomes visible across all implementations.

---

## Lock Free Stats with Virtual Consumers

### SpinRendezvousChannel: 5.8M ops/s, 5-7 cores

**CPU Profile:**
- Total CPU: 24-36 core-seconds over 5 seconds
- All CPU attributed to producers (platform threads)
- Consumers show 0 CPU (ThreadMXBean limitation)

**Per-core efficiency:** ~830K-1,160K ops/s per core used

### SynchronousQueue: 3.1M ops/s, ~7 cores

**CPU Profile:**
- Total CPU: 35-37 core-seconds
- All CPU attributed to producers (89-91% system utilization)
- More cores used than SpinRendezvous but lower throughput

**Efficiency insight:** Using more cores (7 vs 5-7 for SpinRendezvous) but producing less throughput (3.1M vs 5.8M) indicates coordination overhead. The sophisticated algorithm has higher per-operation CPU cost.

**Per-core efficiency:** ~440K-520K ops/s per core used

---

## Interpreting Lock-Based Behavior with Virtual Consumers

### RendezvousChannel: 21.5K ops/s, ~1 core

**CPU Profile:**
- Total CPU: 4.4-5.4 core-seconds
- All CPU on producer side (~0.9-1.1 cores)
- Consumers: 0 CPU (ThreadMXBean limitation)

**Efficiency insight:** The ~1 core usage shows the lock creates a natural throttle.

**Per core efficiency:** ~20K-24K ops/s per core used

### UnBufferedChannel: 16K ops/s, ~1 core

**CPU Profile:**
- Total CPU: 4.4-5.8 core-seconds
- All CPU on producer side (~0.9-1.2 cores)
- High variance (±2,497 ops/s)

**Efficiency insight:** The higher variance compared to RendezvousChannel (±2,497 vs ±2,140) suggests `signalAll()` creates more scheduling jitter. Multiple threads wake up, but only one proceeds while others park again instantly.

**Per-core efficiency:** ~13K-18K ops/s per core used

---

## The ABQ Pattern

### ArrayBlockingQueue: 37K ops/s, ~1.0-1.1 cores

**CPU Profile:**
- Total CPU: 5.1-5.7 core-seconds
- All on producer side: ~1.0-1.1 cores
- Consumers: 0 CPU (ThreadMXBean limitation)

**Why the higher throughput?:**
Despite having a **single lock** (ReentrantLock), ArrayBlockingQueue achieves higher throughput than other lock-based implementations. The circular buffer decouples producers and consumers temporally - a producer can enqueue when the buffer has space, and a consumer can dequeue when items are available, without requiring both to be synchronized at the exact same moment. The buffering means consumers block less frequently and for shorter durations on average, leading to higher overall throughput.

**Efficiency insight:** ArrayBlockingQueue's performance advantage comes from its **buffer**, not from multiple locks. The buffer reduces contention by allowing operations to succeed without both sides needing to coordinate simultaneously.

**Per-core efficiency:** ~34K-37K ops/s per core used (highest among all lock-based implementations due to its buffer nature)

---

## What Virtual Thread CPU Accounting Reveals

### ThreadMXBean Limitations

The zero consumer CPU across all implementations reveals a consistent ThreadMXBean accounting limitation with virtual threads. When virtual threads are mounted on carrier platform threads, the CPU time consumed is not properly attributed back to the virtual thread ID.

**Evidence of measurement artifact:**
- ArrayBlockingQueue: 0 consumer CPU (37K ops/s)
- RendezvousChannel: 0 consumer CPU (21K ops/s)
- UnBufferedChannel: 0 consumer CPU (16K ops/s)
- SpinRendezvousChannel: 0 consumer CPU (5.8M ops/s)
- SynchronousQueue: 0 consumer CPU (3.1M ops/s)

**Platform thread comparison reveals truth:**
When the same benchmarks are run with platform thread consumers, consumer CPU becomes visible across all implementations, showing that the work was always happening - it just wasn't being measured correctly with virtual threads.

### Lock-Free Spinning vs Lock-Based Blocking

**Lock-free with virtual consumers:**
- SpinRendezvousChannel: 0 consumer CPU (measurement artifact), 5.8M ops/s

**Lock-based with virtual consumers:**
- RendezvousChannel: 0 consumer CPU (measurement artifact), 21K ops/s
- Lower throughput but also lower total CPU usage

**Buffered lock-based with virtual consumers:**
- ArrayBlockingQueue: 0 consumer CPU (measurement artifact), 37K ops/s
- Buffer reduces blocking frequency
- Best throughput-per-core among lock-based designs

**Key insight:** The throughput differences come from algorithmic efficiency and contention patterns. The zero consumer CPU tells us nothing about virtual thread efficiency - it's purely a measurement limitation.

---

## Efficiency Metrics Interpretation

### Throughput per Core Used

| Implementation | Throughput/Core | Interpretation |
|----------------|----------------|----------------|
| SpinRendezvousChannel | ~830K-1,160K | Highest efficiency when cores available |
| SynchronousQueue | ~440K-520K | Coordination overhead significant |
| ArrayBlockingQueue | ~34K-37K | Best lock-based efficiency |
| RendezvousChannel | ~20K-24K | Lock serialization with rendezvous overhead |
| UnBufferedChannel | ~13K-18K | Signal overhead adds cost |

**Note:** These numbers only reflect measured producer-side CPU. True efficiency requires platform thread comparison to see total CPU cost.

### CPU Utilization Patterns

**High utilization (61-91%):**
- SpinRendezvousChannel: 5-7 cores
- SynchronousQueue: ~7 cores

**Low utilization (11-15%):**
- All lock-based: ~1 core, serialization creates natural throttle
- ArrayBlockingQueue: 13-14%, highest among lock-based due to buffer

**What this means:** Lock-based channels naturally limit CPU consumption through serialization. However, the zero consumer CPU measurements make these numbers incomplete - see platform thread comparison for full picture.

---

## Design Implications for Virtual Thread Consumers

### Buffered Channels Perform Best

**ArrayBlockingQueue:**
- Highest throughput among lock-based designs (37K vs 16-21K)
- Buffer reduces contention and blocking frequency
- Best throughput-per-core among lock-based approaches

**Why:** Buffer allows temporal decoupling - operations succeed without requiring both sides to coordinate simultaneously

### Performance Hierarchy

1. **Lock-free (5.8M-3.1M ops/s):** Highest throughput via spinning
2. **Buffered lock-based (37K ops/s):** Best lock-based throughput via reduced blocking
3. **Rendezvous lock-based (21K ops/s):** Single lock with signal() efficiency
4. **SignalAll lock-based (16K ops/s):** Thundering herd overhead

---

## Variance Patterns with Virtual Consumers

| Implementation | Variance (CV) | Interpretation |
|----------------|--------------|----------------|
| SpinRendezvousChannel | ~16% | Moderate variance from scheduling |
| SynchronousQueue | ~7% | Complex coordination but stable |
| ArrayBlockingQueue | ~4% | Buffering smooths timing variations |
| RendezvousChannel | ~10% | Lock serialization mostly stable |
| UnBufferedChannel | ~16% | signalAll() creates higher jitter |

**Pattern:** Buffering (ABQ) provides the most stable performance. SignalAll creates the most variance.

---

## Summary

### About ThreadMXBean and Virtual Threads
1. **ThreadMXBean limitation:** Zero consumer CPU is a measurement artifact - virtual threads executing on carriers aren't properly tracked by thread ID
2. **Producer CPU is accurate:** Platform thread producers show accurate CPU measurements regardless of consumer thread type
3. **Platform comparison required:** To understand true CPU cost, benchmark with platform thread consumers (see comparison document)
4. **All implementations affected equally:** The measurement artifact is universal across lock-based and lock-free designs

### About Channel Designs
1. **Lock-free = highest throughput:** 3-6M ops/s via spinning
2. **Buffered lock-based = best lock-based efficiency:** ABQ's 37K ops/s from buffer reducing blocking frequency
3. **Rendezvous lock-based = moderate throughput:** 21K ops/s with signal() efficiency
4. **signalAll() creates jitter:** Even with efficient parking, unnecessary wakeups hurt performance and stability

### About Real Performance Characteristics
1. **Throughput hierarchy holds:** Lock-free > buffered lock-based > rendezvous > signalAll regardless of thread type
2. **Buffering matters most:** ABQ's buffer provides 2x throughput over rendezvous approaches
3. **Wake-up latency revealed in platform comparison:** Virtual threads add 12-21μs per park/unpark for lock-based channels (see platform document)
4. **Thread type selection matters:** For sub-100μs operations with frequent blocking, platform threads show 35-52% gains

---

## Understanding the Complete Picture

These benchmarks with virtual thread consumers reveal throughput characteristics but obscure true CPU costs due to ThreadMXBean limitations. Key findings:

**Throughput insights (reliable):**
- SpinRendezvousChannel: 5.8M ops/s (best overall)
- SynchronousQueue: 3.1M ops/s (lock-free but coordination overhead)
- ArrayBlockingQueue: 37K ops/s (best lock-based via buffering)
- RendezvousChannel: 21K ops/s (clean rendezvous pattern)
- UnBufferedChannel: 16K ops/s (signalAll overhead)

**CPU insights (incomplete):**
- Producer CPU measurements are accurate
- Consumer CPU appears as zero due to ThreadMXBean limitation
- True CPU cost requires platform thread comparison

**When virtual threads excel:**
- I/O-bound workloads with millisecond-scale blocking
- High concurrency requirements (thousands of threads)
- Operations where wake-up latency is negligible compared to work duration

**When platform threads win:**
- CPU-bound coordination with microsecond-scale operations
- Lock-based channels with frequent park/unpark cycles (+35-52% throughput)
- Applications where the 12-21μs wake-up latency compounds significantly

The buffer in ArrayBlockingQueue emerges as the key design element - it reduces blocking frequency enough that thread type becomes nearly irrelevant, achieving solid throughput with either virtual or platform consumers.