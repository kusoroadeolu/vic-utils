# Concurrent Channel Benchmarks

This document summarizes throughput benchmarks comparing **UnBufferedChannel** (a Go-style CSP channel implementation) vs **ArrayBlockingQueue** under different thread configurations.

---

## Benchmark Setup

- **JMH version:** 1.37
- **JVM:** Java 25, HotSpot 64-Bit Server VM
- **Benchmark mode:** Throughput (ops/s)
- **Warmup:** 5 iterations × 10s each
- **Measurement:** 5 iterations × 10s each
- **Thread model:** Virtual threads for consumers, platform threads for producers

---

## 1 Producer / 1 Consumer (1P–1C)

| Implementation | Avg Throughput (ops/s) | Min / Max | StdDev | 99.9% CI |
|----------------|------------------------|-----------|--------|----------|
| **UnBufferedChannel** | 93,124 | 60,367 / 126,846 | 23,803 | [67,678 … 118,571] |
| **ArrayBlockingQueue** | 63,507 | 55,101 / 71,650 | 5,829 | [54,695 … 72,319] |

**Winner:** UnBufferedChannel (**1.5× faster**)

**Key Observations:**
- UnBufferedChannel's leaner implementation excels in single-producer/single-consumer scenarios
- ABQ carries additional generality overhead that impacts performance in simple cases
- Higher variance in channel results (~25% coefficient of variation vs ~9% for ABQ)
- Both primitives show stable operation with no deadlocks or stalls

---

## 4 Producers / 4 Consumers (4P–4C)

### Initial Implementation

| Implementation | Avg Throughput (ops/s) | Min / Max | StdDev | 99.9% CI |
|----------------|------------------------|-----------|--------|----------|
| **UnBufferedChannel** | 11,744 | 7,985 / 24,604 | 4,611 | [4,773 … 18,716] |
| **ArrayBlockingQueue** | 41,084 | 37,122 / 45,642 | 2,486 | [37,326 … 44,842] |

**Winner:** ArrayBlockingQueue (**3.5× faster**)

### Optimized Implementation (Item Consumed Condition)

After adding a dedicated `itemConsumed` condition variable to reduce unnecessary wakeups:

| Implementation | Avg Throughput (ops/s) | Min / Max | StdDev | 99.9% CI |
|----------------|------------------------|-----------|--------|----------|
| **UnBufferedChannel (optimized)** | ~21,850 | 19,962 / 22,477 | ~512 | [~19,910 … ~24,112] |
| **ArrayBlockingQueue** | 41,084 | 37,122 / 45,642 | 2,486 | [37,326 … 44,842] |

**Winner:** ArrayBlockingQueue (**1.9× faster**)

**Key Observations:**
- Optimization nearly **doubled** UnBufferedChannel throughput (11.7k → 21.8k ops/s)
- Significantly reduced variance after optimization
- UnBufferedChannel shows **8× throughput degradation** under contention (93k → 11.7k initial)
- ABQ shows only **1.5× degradation** (63.5k → 41k), demonstrating better contention handling
- ABQ's fairness policies and optimized signaling reduce thundering herd effects
- UnBufferedChannel requires `signalAll()` to avoid lost wakeups, causing performance overhead

---

## Performance Breakdown

### Single Thread Scaling
- **UnBufferedChannel:** 93k → 21.8k ops/s (**76% drop**)
- **ArrayBlockingQueue:** 63.5k → 41k ops/s (**35% drop**)

The double-block handshake pattern in UnBufferedChannel (block → add → block until consumed) amplifies lock contention with multiple threads, while ABQ's simpler buffered model scales better.

---



## Recommendations

### Use UnBufferedChannel when:
- Single producer/single consumer pattern
- Low-latency handoff is critical
- Go-style CSP semantics are desired
- Thread count is low (1-2 per side)

### Use ArrayBlockingQueue when:
- Multiple producers/consumers (3+ threads)
- Consistent performance under contention is required
- Predictable latency is important
- Integration with existing Java concurrent utilities

---

## Lessons Learned

1. **Lock contention kills throughput** - The single `ReentrantLock` becomes a severe bottleneck under high contention
2. **signalAll() is expensive** - Waking all waiting threads creates thundering herd effects that degrade performance
3. **Handshake patterns don't scale** - The double-block coordination that works great for 1P-1C amplifies contention with more threads
4. **JDK implementations are battle-tested** - ABQ's decades of optimization show in its superior contention handling
5. **Variance matters** - Predictable performance is often more valuable than peak throughput

---

## Future Improvements

Potential optimizations for UnBufferedChannel:
- Separate locks for producers vs consumers (dual-lock queue pattern)
- Smart tracking to use `signal()` instead of `signalAll()` where safe
- Lock-free fast paths for uncontended cases
- Per-thread wait tracking to minimize unnecessary wake-ups