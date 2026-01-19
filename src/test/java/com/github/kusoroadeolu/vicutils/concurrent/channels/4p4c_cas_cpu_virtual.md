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

**What this tells us:** Virtual threads yield efficiently when blocked. Rather than burning CPU cycles waiting, the virtual thread scheduler parks these threads when they hit a blocking primitive e.g. `LockSupport#park`, `Object#wait` , making them appear to consume no CPU time. This is universal behavior across all channel types I measured.

**Key insight:** There is no exception here. Virtual thread consumers park so efficiently that they show zero CPU time regardless of the synchronization mechanism used. All consumer work appears "free" from a CPU accounting perspective.

---

## Interpreting Lock Free Behavior with Virtual Consumers

### SpinRendezvousChannel: 5.8M ops/s, 5-7 cores

**CPU Profile:**
- Total CPU: 24-36 coreseconds over 5 seconds
- All CPU attributed to producers
- Consumers show 0 CPU despite actively receiving 5.8M items/sec

**What's happening:**
Virtual threads must explicitly yield or park when spinning. The zero CPU usage indicates the consumer spin loop includes a block park or yield point

**Efficiency insight:** Getting 5.8M ops/s while only showing producer CPU usage suggests the handoff mechanism is extremely efficient once a consumer is available. 

**Per-core efficiency:** ~830K-1,160K ops/s per core used

### SynchronousQueue: 3.1M ops/s, ~7 cores

**CPU Profile:**
- Total CPU: 35-37 core-seconds
- All CPU attributed to producers (89-91% system utilization)
- More cores used than SpinRendezvous but lower throughput

**What's happening:**
SynchronousQueue's dual mode algorithm (stack/queue) creates more complex coordination that keeps all CPU on the producer side.

**Efficiency insight:** Using more cores (7 vs 5-7 for SpinRendezvous) but producing less throughput (3.1M vs 5.8M) indicates coordination overhead. The sophisticated algorithm has higher per operation CPU cost.

**Per-core efficiency:** ~440K-520K ops/s per core used

---

## Interpreting Lock-Based Behavior with Virtual Consumers

### RendezvousChannel: 21.5K ops/s, ~1 core

**CPU Profile:**
- Total CPU: 4.4-5.4 core-seconds
- All CPU on producer side (~0.9-1.1 cores)
- Consumers: 0 CPU despite active receiving

**What's happening:**
The single lock design forces serialization. When a consumer blocks on `receiveReady.await()`, the virtual thread parks completely. The entire operation is driven by producers acquiring the lock, finding or waiting for consumers, and signaling. Virtual thread consumers wake up just long enough to complete the handoff, then immediately park again.

**Efficiency insight:** The zero consumer CPU reveals that lock-based waiting is extremely efficient for virtual threads—they're not spinning, not polling, just cleanly parked until signaled. The ~1 core usage shows the lock creates a natural throttle.

**Per core efficiency:** ~20K-24K ops/s per core used

### UnBufferedChannel: 16K ops/s, ~1 core

**CPU Profile:**
- Total CPU: 4.4-5.8 core-seconds
- All CPU on producer side (~0.9-1.2 cores)
- High variance (±2,497 ops/s)

**What's happening:**
Similar to RendezvousChannel, but the `signalAll()` strategy creates additional wakeup work. However, virtual threads getting spuriously woken up don't register CPU time—they wake, check the condition, and immediately park again without burning cycles.

**Efficiency insight:** The higher variance compared to RendezvousChannel (±2,497 vs ±2,140) suggests `signalAll()` creates more scheduling jitter even with virtual threads. Multiple threads wake up, but only one proceeds while others park again instantly.

**Per-core efficiency:** ~13K-18K ops/s per core used

---

## The ArrayBlockingQueue Pattern

### ArrayBlockingQueue: 37K ops/s, ~1.0-1.1 cores

**CPU Profile:**
- Total CPU: 5.1-5.7 core-seconds
- All on producer side: ~1.0-1.1 cores
- Consumers: 0 CPU (same as all other implementations)

**What's happening:**
Despite having a **single lock** (ReentrantLock), ArrayBlockingQueue achieves higher throughput than other lock-based implementations. The circular buffer decouples producers and consumers temporally—a producer can enqueue when the buffer has space, and a consumer can dequeue when items are available, without requiring both to be synchronized at the exact same moment.

Virtual consumers still park when they block on `take()`, showing zero CPU time. However, the buffering means consumers block less frequently and for shorter durations on average, leading to higher overall throughput.

**Efficiency insight:** ArrayBlockingQueue's performance advantage comes from its **buffer**, not from multiple locks. The buffer reduces contention by allowing operations to succeed without both sides needing to coordinate simultaneously. Virtual threads still park efficiently, but they spend less time parked overall.

**Per-core efficiency:** ~34K-37K ops/s per core used (highest among all lock-based implementations!)

**Implementation note:** ArrayBlockingQueue uses a single `ReentrantLock` for both put and take operations. LinkedBlockingQueue uses two locks (putLock and takeLock), but ABQ's design prioritizes simplicity and cache efficiency with a single lock protecting a bounded circular array.

---

## What Virtual Thread CPU Accounting Reveals

### Universal Parking Behavior

Virtual threads park aggressively across all implementations when they would otherwise block or spin. There is no "parking threshold" that varies by implementation instead, the JVM's virtual thread scheduler consistently removes threads from carriers when they encounter blocking operations.

**Evidence:**
- ArrayBlockingQueue: 0 consumer CPU (37K ops/s)
- RendezvousChannel: 0 consumer CPU (21K ops/s)
- UnBufferedChannel: 0 consumer CPU (16K ops/s)
- SpinRendezvousChannel: 0 consumer CPU (5.8M ops/s)
- SynchronousQueue: 0 consumer CPU (3.1M ops/s)

The difference in throughput comes from how frequently consumers block and for how long, not from whether they park.

### Lock-Free Spinning vs Lock-Based Blocking

**Lock-free with virtual consumers:**
- SpinRendezvousChannel: 0 consumer CPU, 5.8M ops/s
- Virtual threads recognize spinning futility and park
- All throughput driven by producer-side spinning

**Lock-based with virtual consumers:**
- RendezvousChannel: 0 consumer CPU, 21K ops/s
- Virtual threads park cleanly on locks
- Lower throughput but also lower total CPU usage

**Buffered lock-based with virtual consumers:**
- ArrayBlockingQueue: 0 consumer CPU, 37K ops/s
- Buffer reduces blocking frequency
- Best throughput-per-core among lock-based designs

**Key insight:** Virtual threads make blocking operations CPU efficient. The throughput differences come from algorithmic efficiency and contention patterns, not from CPU waste.

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

**What this means:** Lock free designs achieve the highest throughput-per-core, but lock based designs with buffering (ABQ) can achieve surprisingly good efficiency by reducing blocking frequency. The buffer is the key differentiator for ABQ, not multiple locks.

### CPU Utilization Patterns

**High utilization (61-91%):**
- SpinRendezvousChannel: 5-7 cores, mostly productive spinning
- SynchronousQueue: ~7 cores, algorithm coordination overhead

**Low utilization (11-15%):**
- All lock-based: ~1 core, serialization creates natural throttle
- ArrayBlockingQueue: 13-14%, highest among lock-based due to buffer

**What this means:** Lock-based channels naturally limit CPU consumption through serialization. With virtual thread consumers showing zero CPU, this is nearly "free" throughput—minimal CPU cost per operation relative to work done.

---

## Virtual Thread Advantages Revealed

### 1. Universal Zero-Cost Blocking
All implementations show consumers doing substantial work (16K-5.8M receives/sec) while consuming zero CPU. This is the virtual thread value proposition: blocking operations don't waste OS thread resources, period.

### 2. Spin Recognition
SpinRendezvousChannel consumers recognize futile spinning and park automatically. No manual back-off logic needed—the scheduler handles it.

### 3. Buffering Reduces Blocking Frequency
ArrayBlockingQueue achieves the highest lock-based throughput not by avoiding parking (consumers still show 0 CPU), but by reducing how often consumers need to park. The buffer allows asynchronous progress.

---

## Design Implications for Virtual Thread Consumers

### When Virtual Consumers Work Best

**Buffered channels (ArrayBlockingQueue):**
- Highest throughput among lock-based designs (37K vs 16-21K)
- Buffer reduces contention and blocking frequency
- Consumers still park (0 CPU) but block less often
- Best throughput-per-core among lock-based approaches

**Why:** Buffer allows temporal decoupling operations succeed without requiring both sides to coordinate simultaneously

### When Virtual Consumers Are Invisible

**All implementations:**
- Virtual threads park so efficiently they show 0 CPU across all designs
- Consumers contribute zero to CPU accounting
- "Free" consumer work from a CPU perspective

**Why:** Virtual thread scheduler removes threads from carriers during any blocking operation

### Performance Hierarchy

1. **Lock-free (5.8M-3.1M ops/s):** Highest throughput via spinning, all CPU on producer side
2. **Buffered lock-based (37K ops/s):** Best lock-based throughput via reduced blocking
3. **Rendezvous lock-based (21K ops/s):** Single lock with signal() efficiency
4. **SignalAll lock-based (16K ops/s):** Thundering herd overhead even with virtual threads

---

## Variance Patterns with Virtual Consumers

| Implementation | Variance (CV) | Interpretation |
|----------------|--------------|----------------|
| SpinRendezvousChannel | ~16% | Virtual consumer scheduling adds jitter |
| SynchronousQueue | ~7% | Complex coordination but stable |
| ArrayBlockingQueue | ~4% | Buffering smooths timing variations |
| RendezvousChannel | ~10% | Lock serialization mostly stable |
| UnBufferedChannel | ~16% | signalAll() + virtual scheduling = higher jitter |

**Pattern:** Buffering (ABQ) provides the most stable performance. SignalAll creates the most variance. Lock-free designs have moderate variance from virtual thread scheduling decisions.

---

## Summary

### About Virtual Threads
1. **They park universally:** Zero consumer CPU across all 5 implementations shows consistent, efficient scheduling
2. **They recognize all futile work:** Spinning or blocking, virtual threads park rather than burn cycles
3. **Parking is not the bottleneck:** Throughput differences come from blocking frequency, not parking overhead
4. **They're nearly free when blocked:** Lock-based channels get work done with minimal CPU cost

### About Channel Designs
1. **Lock-free + virtual consumers = producer-driven throughput:** All CPU on producer side, achieving 3-6M ops/s
2. **Single lock + buffer + virtual consumers = best lock-based efficiency:** ABQ's 37K ops/s from buffering, not dual locks
3. **Single lock + rendezvous + virtual consumers = moderate throughput:** 21K ops/s with signal() efficiency
4. **signalAll() creates both jitter and overhead:** Even with virtual threads, unnecessary wakeups hurt performance

### About CPU Efficiency Measurement
1. **Zero CPU ≠ zero work:** Virtual consumers do 16K-5.8M operations while showing 0 CPU time
2. **Throughput-per-core matters more:** ABQ's 34K-37K/core is best among lock-based despite absolute throughput differences
3. **System utilization misleading:** 91% utilization (SynchronousQueue) vs 14% (ArrayBlockingQueue) doesn't account for virtual thread efficiency
4. **Buffer reduces blocking frequency:** ABQ's higher throughput comes from less frequent parking, not from avoiding parking

---

## Virtual Thread's Value Proposition

These benchmarks demonstrate virtual threads' core benefit: **blocking becomes cheap**.

- RendezvousChannel: 21.5K ops/s with ~1 core
- UnBufferedChannel: 16K ops/s with ~1 core 
- ArrayBlockingQueue: 37K ops/s with ~1 core 
- SpinRendezvousChannel: 5.8M ops/s with 5-7 cores 

Traditional platform threads would show consumer CPU in all cases. Virtual threads show us the true computational cost: just the producer side coordination work. Everything else is scheduling overhead that virtual threads minimize.

This is why lock-based channels look more attractive with virtual threads than raw throughput suggests: you're getting 16K-37K operations per second at nearly zero CPU cost for the consumer side. The buffer in ArrayBlockingQueue is the differentiator among lock based designs, achieving the best efficiency by reducing how often consumers need to block.