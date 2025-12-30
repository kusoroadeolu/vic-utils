# Concurrent Channel Benchmarks

This document summarizes throughput benchmarks comparing **UnBufferedChannel** vs **ArrayBlockingQueue** under different thread configurations.

---

## 1 Producer / 1 Consumer (1P–1C)

| Benchmark | Avg Throughput (ops/s) | Min / Max | StdDev | 99.9% CI |
|-----------|----------------------|-----------|--------|-----------|
| **UnBufferedChannel** | 93,124 | 60,367 / 126,846 | 23,803 | [67,678 … 118,571] |
| **ArrayBlockingQueue** | 63,507 | 55,101 / 71,650 | 5,829 | [54,695 … 72,319] |

**Observations:**

- UnBufferedChannel outperforms ABQ in **single-producer/single-consumer** scenarios.
- Slightly higher variance in channel results due to thread scheduling.
- Both are stable and free of stalls.

---

## 4 Producers / 4 Consumers (4P–4C)

| Benchmark | Avg Throughput (ops/s) | Min / Max | StdDev | 99.9% CI |
|-----------|----------------------|-----------|--------|-----------|
| **UnBufferedChannel** | 11,744 | 7,985 / 24,604 | 4,611 | [4,773 … 18,716] |
| **ArrayBlockingQueue** | 41,084 | 37,122 / 45,642 | 2,486 | [37,326 … 44,842] |

**Observations:**

- Throughput drops significantly for **UnBufferedChannel** under contention (~8× drop).
- ArrayBlockingQueue handles 4P–4C better due to its internal buffering and fair lock management.
- UnBufferedChannel shows higher variance; ABQ results are more predictable.
- Use unbuffered channels for low-thread, high-frequency handoffs. ABQ is better for multi-threaded scenarios.

---

## Summary

- **UnBufferedChannel** excels in 1P–1C scenarios where low-latency, handoff performance is critical.
- **ArrayBlockingQueue** provides better throughput stability under higher thread contention (4P–4C).
- Thread count and contention drastically affect unbuffered channel performance.
- Variance in measurements is an important factor when deciding which primitive to use.
