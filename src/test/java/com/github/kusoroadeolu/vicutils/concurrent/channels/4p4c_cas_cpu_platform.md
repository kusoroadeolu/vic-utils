# CPU Efficiency Analysis - Platform Thread Comparison (4P/4C)

This document analyzes how switching from virtual thread consumers to platform thread consumers changes CPU efficiency and behavior patterns in the **4 producer / 4 consumer** configuration.

---

## Benchmark Setup

- **JMH version:** 1.37
- **JVM:** Java 25, HotSpot 64-Bit Server VM (25+37-LTS-3491)
- **Benchmark mode:** Throughput (ops/s)
- **Warmup:** 3 iterations × 5s each
- **Measurement:** 3 iterations × 5s each
- **Forks:** 3
- **Thread configuration:** 4 producers (platform threads) / 4 consumers (platform threads)
- **CPU tracking:** Measured via ThreadMXBean for producer and consumer threads separately

---

## Results Comparison: Virtual vs Platform Thread Consumers

| Implementation | Virtual Consumers | Platform Consumers | Throughput Change | CPU Pattern Change |
|----------------|-------------------|--------------------|--------------------|-------------------|
| **SpinRendezvousChannel** | 5.80M ops/s, 5-7 cores | 6.46M ops/s, 7-7.5 cores | +11.4% | Consumer CPU now visible (~3.6 cores) |
| **SynchronousQueue** | 3.11M ops/s, ~7 cores | 3.23M ops/s, 6-7 cores | +3.9% | Consumer CPU now visible (~3.3 cores) |
| **ArrayBlockingQueue** | 37.1K ops/s, ~1.0-1.1 cores | 38.3K ops/s, ~0.7 cores | +3.2% | Consumer CPU now visible (~0.36 cores) |
| **RendezvousChannel** | 21.5K ops/s, ~0.9-1.1 cores | 29.2K ops/s, ~0.75 core | +35.7% | Consumer CPU now visible (~0.3 cores) |
| **UnBufferedChannel** | 16.0K ops/s, ~0.9-1.2 cores | 24.4K ops/s, ~0.7-1.0 core | +52.5% | Consumer CPU now visible (~0.25 cores) |

---

## The Appearance of Consumer CPU Usage

### Before: Virtual Thread Consumers (0 CPU)

With virtual consumers, **all five implementations showed zero consumer CPU**:
- SpinRendezvousChannel
- SynchronousQueue
- ArrayBlockingQueue
- RendezvousChannel
- UnBufferedChannel

### After: Platform Thread Consumers (Visible CPU)

With platform consumers, **all implementations now show consumer CPU usage:**

**SpinRendezvousChannel:**
- Producer cores: ~3.5-3.7 cores
- Consumer cores: ~3.5-3.7 cores
- Total: ~7.0-7.4 cores (balanced)

**SynchronousQueue:**
- Producer cores: ~2.3-3.7 cores
- Consumer cores: ~2.3-3.7 cores
- Total: ~4.5-7.3 cores (balanced, highly variable)

**ArrayBlockingQueue:**
- Producer cores: ~0.34-0.45 cores
- Consumer cores: ~0.29-0.40 cores
- Total: ~0.67-0.77 cores (balanced)

**RendezvousChannel:**
- Producer cores: ~0.42-0.53 cores
- Consumer cores: ~0.25-0.33 cores
- Total: ~0.73-0.84 cores

**UnBufferedChannel:**
- Producer cores: ~0.42-0.78 cores
- Consumer cores: ~0.19-0.32 cores
- Total: ~0.66-1.04 cores

**What this reveals:** Platform threads consume CPU time even when blocked or spinning, unlike virtual threads which park efficiently. The "hidden work" of consumer threads is now visible in the measurements.

---

## Throughput Changes and What They Mean

### Lock-Free: Modest Gains (+3.9% to +11.4%)

**SpinRendezvousChannel: +11.4%**
- Virtual: 5.80M ops/s
- Platform: 6.46M ops/s
- **Why:** Platform threads can spin continuously without being preemptively parked by a virtual thread scheduler. Spinning is more consistent without scheduler intervention.

**SynchronousQueue: +3.9%**
- Virtual: 3.11M ops/s
- Platform: 3.23M ops/s
- **Why:** Minimal gain because the complex dual-mode algorithm already does parking/unparking internally. Platform threads just reduce the virtual thread scheduling overhead slightly.

**Interpretation:** Lock-free approaches benefit modestly from platform threads because continuous spinning is more effective without scheduler intervention.

### Lock-Based: Major Gains (+3.2% to +52.5%)

**RendezvousChannel: +35.7%**
- Virtual: 21.5K ops/s
- Platform: 29.2K ops/s
- **Why:** Platform threads wake up faster from `LockSupport.park()` than virtual threads can be rescheduled. The wake-up latency reduction compounds over millions of operations.

**UnBufferedChannel: +52.5%**
- Virtual: 16.0K ops/s
- Platform: 24.4K ops/s
- **Why:** Similar to RendezvousChannel, but the `signalAll()` strategy amplifies the benefit. Multiple platform threads can be woken and ready to run immediately, while virtual threads need scheduler intervention.

**ArrayBlockingQueue: +3.2%**
- Virtual: 37.1K ops/s
- Platform: 38.3K ops/s
- **Why:** Buffering keeps operations fast enough that virtual thread scheduling overhead is minimal. Platform threads add only a slight edge.

**Interpretation:** Lock-based channels benefit dramatically from platform threads because wake-up latency matters more than spin efficiency. The buffer in ArrayBlockingQueue reduces blocking frequency enough that virtual thread scheduling overhead becomes negligible.

---

## CPU Efficiency Shifts

### Virtual Thread Baseline: "Free" Consumer Work

With virtual consumers, **all implementations showed zero consumer CPU**:
- ArrayBlockingQueue: 37.1K ops/s using ~1.0-1.1 producer cores (consumers showed 0)
- RendezvousChannel: 21.5K ops/s using ~0.9-1.1 producer cores (consumers showed 0)
- UnBufferedChannel: 16K ops/s using ~0.9-1.2 producer cores (consumers showed 0)

**Apparent efficiency:** Nearly infinite throughput-per-consumer-core (dividing by zero)

### Platform Thread Reality: True CPU Cost

With platform consumers:
- ArrayBlockingQueue: 38.3K ops/s using ~0.67-0.77 total cores (~0.34-0.45 producer + ~0.29-0.40 consumer)
- RendezvousChannel: 29.2K ops/s using ~0.73-0.84 total cores (~0.42-0.53 producer + ~0.25-0.33 consumer)
- UnBufferedChannel: 24.4K ops/s using ~0.66-1.04 total cores (~0.42-0.78 producer + ~0.19-0.32 consumer)

**Real efficiency:**
- ArrayBlockingQueue: ~50K-57K ops/s per core
- RendezvousChannel: ~35K-40K ops/s per core
- UnBufferedChannel: ~23K-37K ops/s per core

**What this reveals:** Virtual threads made consumer-side work appear "free" in CPU accounting, but platform threads show the true computational cost. However, this "revealed" cost is still quite low—well under 1 core total for 24K-38K operations per second.

---

## What Changed in Spin-Based Implementations

### SpinRendezvousChannel: Now Both Sides Spin

**Virtual consumers (before):**
- All CPU on producer side (~5-7 cores)
- Consumers: 0 CPU (parked when they would spin)
- 5.80M ops/s

**Platform consumers (now):**
- Balanced CPU: ~3.5-3.7 producer cores, ~3.5-3.7 consumer cores
- Both sides actively spinning
- 6.46M ops/s (+11.4%)

**What this tells us:**

Virtual thread scheduler was parking spinning consumers. The 0 CPU wasn't because consumers weren't trying to spin—they were being preemptively scheduled off cores to avoid waste. Platform threads spin continuously, keeping cores hot and ready. The 11.4% gain comes from reduced scheduling overhead.

### SynchronousQueue: Minimal Change Despite Complexity

**Virtual consumers (before):**
- ~7 cores, all attributed to producers
- 3.11M ops/s

**Platform consumers (now):**
- ~2.3-3.7 cores per side, balanced between producers and consumers (highly variable)
- 3.23M ops/s (+3.9%)

**What this tells us:**

Internal parking already handled most scheduling—SynchronousQueue's dual-mode algorithm uses `LockSupport.park()` internally, so the difference between virtual and platform threads is just the scheduling layer overhead. Variance increased from ~7% to ~10% because platform threads are subject to OS scheduler decisions rather than the more controlled virtual thread scheduler.

---

## What Changed in Lock-Based Implementations

### RendezvousChannel: Major Throughput Gain, Lower Total CPU

**Virtual consumers (before):**
- ~0.9-1.1 cores, all producer-side
- 21.5K ops/s
- Consumer CPU: 0

**Platform consumers (now):**
- ~0.73-0.84 cores total (~0.42-0.53 producer, ~0.25-0.33 consumer)
- 29.2K ops/s (+35.7%)
- Both sides showing CPU

**What this tells us:**

Wake-up latency dominated performance. The 35.7% throughput increase with actually *lower* total CPU usage (~0.73-0.84 vs ~0.9-1.1 cores) shows that virtual thread rescheduling latency was the bottleneck, not computational cost. Platform threads can park/unpark in microseconds without scheduler overhead.

### UnBufferedChannel: Massive Gain from signalAll() Optimization

**Virtual consumers (before):**
- ~0.9-1.2 cores, all producer-side
- 16K ops/s
- Consumer CPU: 0

**Platform consumers (now):**
- ~0.66-1.04 cores total (~0.42-0.78 producer, ~0.19-0.32 consumer)
- 24.4K ops/s (+52.5%)
- Both sides showing CPU

**What this tells us:**

The 52.5% gain is the largest of any implementation. `signalAll()` is much faster with platform threads—waking all waiting threads happens in kernel space for platform threads, while virtual threads need scheduler intervention for each thread. However, variance increased from ~16% to ~25%, suggesting platform thread wake-up timing is less predictable than virtual thread scheduling.

### ArrayBlockingQueue: The Buffer Makes Thread Type Irrelevant

**Virtual consumers (before):**
- ~1.0-1.1 cores, all producer-side
- 37.1K ops/s
- Consumer CPU: 0

**Platform consumers (now):**
- ~0.67-0.77 cores (~0.34-0.45 producer, ~0.29-0.40 consumer)
- 38.3K ops/s (+3.2%)
- Both sides showing CPU

**What this tells us:**

The minimal 3.2% gain shows that buffering eliminated the virtual thread penalty. The circular buffer decouples timing so that operations complete quickly—threads rarely block long enough for virtual thread scheduling overhead to matter. This is the "sweet spot" for virtual threads: when buffer decoupling keeps lock hold times short, virtual threads perform identically to platform threads while using fewer OS resources.

---

## Understanding the CPU Accounting Differences

### Why Virtual Thread Consumer CPU Was Zero

**Not because consumers weren't working:**
- ArrayBlockingQueue: 37.1K receives/sec
- RendezvousChannel: 21.5K receives/sec
- UnBufferedChannel: 16K receives/sec
- SpinRendezvousChannel: 5.8M receives/sec

**But because virtual threads park aggressively:**

When a virtual thread encounters a lock, spin loop, or any blocking operation, the virtual thread scheduler removes it from the platform thread carrier and mounts a different virtual thread. The blocked thread shows zero CPU time even though it's actively waiting and will resume work.

### Why Platform Thread Consumer CPU Is Visible

Platform threads consume CPU while blocked. When a platform thread blocks on `LockSupport.park()`, a spin loop, or lock contention, it still "owns" the OS thread. The CPU time represents actual spin cycles (lock-free), OS scheduler bookkeeping (lock-based), wake-up overhead, and cache line bouncing.

---

## Variance Pattern Changes

| Implementation | Virtual CV | Platform CV | Change |
|----------------|-----------|-------------|---------|
| SpinRendezvousChannel | ~16% | ~5% | **Improved** |
| SynchronousQueue | ~7% | ~10% | Degraded |
| RendezvousChannel | ~10% | ~10% | Unchanged |
| UnBufferedChannel | ~16% | ~25% | Degraded |
| ArrayBlockingQueue | ~4% | ~6% | Slight degradation |

**Pattern:** Simple lock-free (SpinRendezvous) benefits from platform thread consistency—less scheduling jitter. Complex algorithms (SynchronousQueue) and signalAll() (UnBuffered) suffer from OS scheduler unpredictability.

---

## Efficiency Metrics: The Full Picture

| Implementation | Virtual (ops/core) | Platform (ops/core) | Interpretation |
|----------------|-------------------|---------------------|----------------|
| SpinRendezvousChannel | ~830K-1,160K | ~860K-930K | Similar efficiency |
| SynchronousQueue | ~440K-520K | ~440K-720K | Wide variance with platform |
| ArrayBlockingQueue | ~34K-37K* | ~50K-57K | True efficiency with platform |
| RendezvousChannel | ~20K-24K* | ~35K-40K | True efficiency with platform |
| UnBufferedChannel | ~13K-18K* | ~23K-37K | True efficiency with platform |

*Virtual thread numbers artificially inflated due to zero consumer CPU accounting

**What this reveals:** Lock-based channels' "efficiency" with virtual threads was an accounting illusion. Platform threads show their true efficiency, which is still quite good.

---

## When Platform Threads Matter Most

### High-Benefit Scenarios (+35% to +52%)

**Lock-based channels with frequent park/unpark:**
- RendezvousChannel: +35.7%
- UnBufferedChannel: +52.5%

**Why:** Wake-up latency dominates performance. Platform threads eliminate virtual thread scheduler overhead on every park/unpark cycle.

**Use platform threads when:**
- Operations in the microsecond range
- Frequent blocking/unblocking (rendezvous pattern)
- signalAll() or similar multi-wake patterns

### Low-Benefit Scenarios (+3% to +11%)

**Lock-free and buffered channels:**
- SpinRendezvousChannel: +11.4%
- SynchronousQueue: +3.9%
- ArrayBlockingQueue: +3.2%

**Why:** Either already optimized for spinning (lock-free) or operations fast enough that thread type doesn't matter (buffered).

**Use virtual threads when:**
- High-throughput lock-free operations (already 3-6M+ ops/s)
- Buffered channels with quick operations
- CPU resources are constrained

---

## The Wake-Up Latency Tax

### Estimating Virtual Thread Scheduling Overhead

**RendezvousChannel:**
- Virtual: 21.5K ops/s = ~46.5 μs per operation
- Platform: 29.2K ops/s = ~34.2 μs per operation
- **Overhead: ~12.3 μs per operation** (~26% of total time)

**UnBufferedChannel:**
- Virtual: 16K ops/s = ~62.5 μs per operation
- Platform: 24.4K ops/s = ~41 μs per operation
- **Overhead: ~21.5 μs per operation** (~34% of total time)

**What this means:** Virtual thread scheduling adds 12-21 microseconds per operation for lock-based channels. This is the cost of unmounting virtual thread from carrier, scheduler bookkeeping, remounting when signaled, and cache effects from thread migration.

For operations taking tens of microseconds, this overhead is significant. For operations taking milliseconds (I/O), it's negligible.

---

## Design Implications

### For Lock-Free Channels

**Platform threads:** +11% throughput, both sides actively spinning (7+ cores), lower variance, predictable spin timing.  
**Virtual threads:** Still 3-6M ops/s (excellent), consumers park instead of spin (5-7 cores), higher variance from scheduler decisions.

### For Lock-Based Channels

**Platform threads:** +35-52% throughput (massive improvement), true CPU cost visible (~0.7-1.0 cores), faster wake-up from blocking.  
**Virtual threads:** Lower throughput but CPU-efficient appearance, consumer work "free" in accounting, higher wake-up latency.

### For Buffered Channels

**Either thread type works well:** Minimal difference (3% throughput gain), buffer decoupling keeps lock hold times short, virtual threads fine if resources constrained.

---

## Summary: What Platform Threads Reveal

### About Thread Behavior
1. **Virtual threads park universally:** Zero consumer CPU across all implementations shows consistent, aggressive parking
2. **Platform threads show true cost:** Consumer CPU now visible, but still quite low (0.2-3.7 cores for 24K-6.5M ops/s)
3. **Wake-up latency matters for lock-based designs:** 12-21 μs overhead per park/unpark cycle with virtual threads
4. **Spin consistency matters for lock-free designs:** Platform threads spinning continuously is more predictable than virtual thread parking decisions

### About Implementation Tradeoffs
1. **Lock-free gains modestly:** +3.9% to +11.4% because algorithms already optimized for spinning
2. **Lock-based gains dramatically:** +35.7% to +52.5% because wake-up latency eliminated
3. **Buffering neutralizes thread type differences:** +3.2% when buffer keeps operations fast enough to avoid parking overhead
4. **signalAll() benefits most from platform threads:** +52.5% because multiple wakeups no longer need scheduler intervention

**The key insight:** Virtual threads are excellent for I/O bound work  but have measurable overhead for CPU bound work with microsecond range blocking. Platform threads reveal this overhead by eliminating it.