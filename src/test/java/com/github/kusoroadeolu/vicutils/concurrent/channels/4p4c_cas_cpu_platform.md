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
| **SpinRendezvousChannel** | 5.80M ops/s, 5-7 cores* | 6.46M ops/s, 7-7.5 cores | +11.4% | Consumer CPU now visible (~3.6 cores) |
| **SynchronousQueue** | 3.11M ops/s, ~7 cores* | 3.23M ops/s, 6-7 cores | +3.9% | Consumer CPU now visible (~3.3 cores) |
| **ArrayBlockingQueue** | 37.1K ops/s, ~1.0-1.1 cores* | 38.3K ops/s, ~0.7 cores | +3.2% | Consumer CPU now visible (~0.36 cores) |
| **RendezvousChannel** | 21.5K ops/s, ~0.9-1.1 cores* | 29.2K ops/s, ~0.75 core | +35.7% | Consumer CPU now visible (~0.3 cores) |
| **UnBufferedChannel** | 16.0K ops/s, ~0.9-1.2 cores* | 24.4K ops/s, ~0.7-1.0 core | +52.5% | Consumer CPU now visible (~0.25 cores) |

*Note: Virtual consumer measurements show only producer side CPU due to ThreadMXBean accounting limitations

---

## The Appearance of Consumer CPU Usage

### Before: Virtual Thread Consumers (0 CPU)

With virtual consumers, **all five implementations showed zero consumer CPU**, probably due to ThreadMXBean's inability to properly attribute virtual thread CPU time when queried by thread ID.

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

Platform threads allow ThreadMXBean to properly measure CPU consumption on both sides of the channel. The consumer work was always happening, it just wasn't being measured with virtual threads.

---

## Throughput Changes

### Lock-Free: Modest Gains (+3.9% to +11.4%)

**SpinRendezvousChannel: +11.4%**
- Virtual: 5.80M ops/s
- Platform: 6.46M ops/s

**SynchronousQueue: +3.9%**
- Virtual: 3.11M ops/s
- Platform: 3.23M ops/s

Lock-free approaches show modest gains with platform threads. The throughput improvement suggests reduced scheduling overhead, though the exact mechanisms are difficult to isolate from other factors.

### Lock-Based: Major Gains (+3.2% to +52.5%)

**RendezvousChannel: +35.7%**
- Virtual: 21.5K ops/s
- Platform: 29.2K ops/s

**UnBufferedChannel: +52.5%**
- Virtual: 16.0K ops/s
- Platform: 24.4K ops/s

**ArrayBlockingQueue: +3.2%**
- Virtual: 37.1K ops/s
- Platform: 38.3K ops/s

Lock-based channels show dramatic gains with platform threads, except for ArrayBlockingQueue. The buffer in ArrayBlockingQueue appears to mitigate whatever overhead virtual threads introduce. For rendezvous patterns, the overhead is substantial.

---

## CPU Efficiency Analysis

### ThreadMXBean Accounting Differences

**With virtual consumers (producer CPU only measured):**
- ArrayBlockingQueue: 37.1K ops/s using ~1.0-1.1 cores (producer only measurement)
- RendezvousChannel: 21.5K ops/s using ~0.9-1.1 cores (producer only measurement)
- UnBufferedChannel: 16K ops/s using ~0.9-1.2 cores (producer only measurement)

**With platform consumers (both sides measured):**
- ArrayBlockingQueue: 38.3K ops/s using ~0.67-0.77 total cores
- RendezvousChannel: 29.2K ops/s using ~0.73-0.84 total cores
- UnBufferedChannel: 24.4K ops/s using ~0.66-1.04 total cores

**True efficiency (platform thread measurements):**
- ArrayBlockingQueue: ~50K-57K ops/s per core
- RendezvousChannel: ~35K-40K ops/s per core
- UnBufferedChannel: ~23K-37K ops/s per core

**Key finding:** Platform thread measurements reveal that total CPU usage is remarkably similar across virtual and platform configurations, but throughput differs significantly for lock-based non-buffered channels.

---

## Performance Analysis by Implementation

### SpinRendezvousChannel

**Virtual consumers:**
- 5.80M ops/s
- ~5-7 cores (producer-side measurement only)

**Platform consumers:**
- 6.46M ops/s (+11.4%)
- ~7.0-7.4 cores total (~3.5-3.7 producer, ~3.5-3.7 consumer)

**Observation:** Now that both sides are measured, we see balanced CPU distribution. The 11.4% throughput gain with platform threads suggests some scheduling overhead with virtual threads, though the absolute performance remains excellent in both cases.

### SynchronousQueue

**Virtual consumers:**
- 3.11M ops/s
- ~7 cores (producer-side measurement only)

**Platform consumers:**
- 3.23M ops/s (+3.9%)
- ~4.5-7.3 cores total (highly variable, balanced distribution)

**Observation:** Minimal throughput gain despite switching to platform threads. SynchronousQueue's internal use of `LockSupport.park()` means the coordination mechanism is similar regardless of thread type. Variance increased from ~7% to ~10%.

### RendezvousChannel: Significant Throughput Gain

**Virtual consumers:**
- 21.5K ops/s
- ~0.9-1.1 cores (producer-side measurement only)

**Platform consumers:**
- 29.2K ops/s (+35.7%)
- ~0.73-0.84 cores total (~0.42-0.53 producer, ~0.25-0.33 consumer)

**Key finding:** 35.7% throughput increase while using *less* total CPU (~0.73-0.84 vs ~0.9-1.1 cores). This suggests virtual threads introduce latency overhead that platform threads avoid, but this overhead consumes minimal CPU, it's pure scheduling delay.

### UnBufferedChannel: Largest Throughput Gain

**Virtual consumers:**
- 16K ops/s
- ~0.9-1.2 cores (producer-side measurement only)

**Platform consumers:**
- 24.4K ops/s (+52.5%)
- ~0.66-1.04 cores total (~0.42-0.78 producer, ~0.19-0.32 consumer)

**Key finding:** The largest throughput gain (52.5%) of any implementation. The `rendezvous` pattern appears particularly sensitive to thread type. Variance increased from ~16% to ~25% though negligible is something to watch out for

### ArrayBlockingQueue: Thread Type Nearly Irrelevant

**Virtual consumers:**
- 37.1K ops/s
- ~1.0-1.1 cores (producer-side measurement only)

**Platform consumers:**
- 38.3K ops/s (+3.2%)
- ~0.67-0.77 cores total (~0.34-0.45 producer, ~0.29-0.40 consumer)

**Key finding:** Minimal throughput difference (3.2%). The circular buffer decouples producer and consumer timing enough that thread type has negligible impact on performance. This suggests buffering is an effective strategy for making code thread type agnostic.

---

## Understanding the ThreadMXBean Measurement Differences

### Virtual Thread Consumer CPU Was Zero (Measurement Artifact)

**Not because consumers weren't working:**
- ArrayBlockingQueue: 37.1K receives/sec
- RendezvousChannel: 21.5K receives/sec
- UnBufferedChannel: 16K receives/sec
- SpinRendezvousChannel: 5.8M receives/sec

**But because ThreadMXBean can't track virtual thread CPU by thread ID:**
When virtual threads are mounted on carrier platform threads, `ThreadMXBean.getThreadCpuTime(virtualThreadId)` cannot properly attribute the CPU time consumed. The work happens on the carrier thread, but querying by the virtual thread's ID returns near-zero values.

### Platform Thread Consumer CPU Is Visible (Accurate Measurement)

Platform threads can be tracked accurately by ThreadMXBean. When queried by thread ID, the CPU time reflects actual work done, including:
- Spinning in lock-free loops
- Blocking on locks and condition variables
- OS scheduler overhead
- Wake-up and context switch costs

---

## Variance Pattern Changes

| Implementation | Virtual CV | Platform CV | Change |
|----------------|-----------|-------------|---------|
| SpinRendezvousChannel | ~16% | ~5% | **Improved** |
| SynchronousQueue | ~7% | ~10% | Degraded |
| RendezvousChannel | ~10% | ~10% | Unchanged |
| UnBufferedChannel | ~16% | ~25% | Degraded |
| ArrayBlockingQueue | ~4% | ~6% | Slight degradation |

**Pattern:** Simple lock free (SpinRendezvous) shows improved stability with platform threads. Complex coordination (SynchronousQueue) and signalAll() patterns show increased variance with platform threads, likely due to OS scheduler unpredictability.

---

## Efficiency Metrics Comparison

| Implementation | Virtual (ops/core)* | Platform (ops/core) | Interpretation |
|----------------|-------------------|---------------------|----------------|
| SpinRendezvousChannel | ~830K-1,160K | ~860K-930K | Similar true efficiency |
| SynchronousQueue | ~440K-520K | ~440K-720K | Similar with high variance |
| ArrayBlockingQueue | N/A | ~50K-57K | True efficiency now measurable |
| RendezvousChannel | N/A | ~35K-40K | True efficiency now measurable |
| UnBufferedChannel | N/A | ~23K-37K | True efficiency now measurable |

*Virtual thread measurements incomplete due to zero consumer CPU

**Key insight:** Platform thread measurements provide complete CPU accounting, revealing true per-core efficiency across all implementations.

---

## The Wake-Up Latency Tax

### Estimating Virtual Thread Overhead

**RendezvousChannel:**
- Virtual: 21.5K ops/s = ~46.5 μs per operation
- Platform: 29.2K ops/s = ~34.2 μs per operation
- **Difference: ~12.3 μs per operation** (~26% of total time)

**UnBufferedChannel:**
- Virtual: 16K ops/s = ~62.5 μs per operation
- Platform: 24.4K ops/s = ~41 μs per operation
- **Difference: ~21.5 μs per operation** (~34% of total time)

Virtual threads add roughly 12-21 microseconds of latency per operation for lock-based rendezvous patterns. This overhead likely comes from:
- Loom scheduler bookkeeping
- Unmounting/remounting from carriers
- Additional context switches
- Cache effects from thread migration between carriers

For operations taking tens of microseconds, this overhead is significant (26-34% of total time). For operations taking milliseconds (typical I/O), this overhead becomes negligible (<2%).

---

## When Platform Threads Provide Significant Benefits

### High-Benefit Scenarios (+35% to +52%)

**Lock-based channels with frequent park/unpark:**
- RendezvousChannel: +35.7%
- UnBufferedChannel: +52.5%

**Characteristics of high-benefit workloads:**
- Operations completing in tens of microseconds
- Frequent blocking and unblocking (rendezvous semantics)
- Multiple threads waking simultaneously (signalAll patterns)
- CPU-bound coordination rather than I/O waiting

### Low-Benefit Scenarios (+3% to +11%)

**Lock-free and buffered channels:**
- SpinRendezvousChannel: +11.4%
- SynchronousQueue: +3.9%
- ArrayBlockingQueue: +3.2%

**Characteristics of low-benefit workloads:**
- Already very high throughput (millions of ops/sec)
- Buffered designs that reduce blocking frequency
- Operations fast enough that thread scheduling overhead is proportionally small

---

## Design Implications

### For Lock-Free Channels

**Platform threads:**
- Modest throughput gains (+4-11%)
- Both sides actively working (higher total CPU)
- Better variance (SpinRendezvous: 16% to 5%)

**Virtual threads:**
- Still excellent throughput (3-6M ops/s)
- Measurement challenges with ThreadMXBean
- Slightly higher variance in simple designs

### For Lock-Based Non-Buffered Channels

**Platform threads:**
- Major throughput gains (+35-52%)
- 12-21 μs faster per operation
- Complete CPU accounting

**Virtual threads:**
- Significant latency overhead for sub-100μs operations
- Still appropriate if operations are I/O-bound
- Lower absolute throughput for CPU-bound coordination

### For Buffered Channels

**Platform threads:**
- Minimal gains (+3.2%)
- Complete CPU measurement

**Virtual threads:**
- Nearly identical performance
- Measurement limitations don't affect design decisions
- Better resource utilization for high concurrency

---

## Summary

### About Measurement
1. **ThreadMXBean limitation hypothesis:** Virtual thread CPU cannot be measured by thread ID
2. **Platform threads enable complete accounting:** Both producer and consumer CPU now visible
3. **Total CPU usage is similar:** Virtual and platform configurations use comparable CPU
4. **Throughput differences are real:** Platform threads eliminate 12-21μs of latency overhead

### About Performance Characteristics
1. **Lock-free minimal gains:** +4-11% throughput
2. **Lock-based dramatic gains:** +35-52% throughput, wake up latency eliminated
3. **Buffering neutralizes differences:** +3.2% when blocking is infrequent
4. **signalAll() most sensitive:** +52.5% gain, multiple wake-ups benefit most

### About Virtual Thread Overhead
1. **Quantified latency tax:** 12-21 μs per park/unpark cycle for lock-based channels
2. **Matters for microsecond operations:** 26-34% of total time for sub-100μs work
3. **Negligible for I/O:** <2% overhead for millisecond-scale operations
4. **Buffer provides workaround:** Reducing park frequency eliminates the penalty

**The buffer insight:** ArrayBlockingQueue demonstrates that simple buffering can make code thread-type agnostic, achieving good performance with either virtual or platform threads.