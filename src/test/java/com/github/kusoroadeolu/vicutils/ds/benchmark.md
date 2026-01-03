# Benchmark Results

## Test Environment
- **JVM**: Java HotSpot 64-Bit Server VM, JDK 25+37-LTS-3491
- **JMH Version**: 1.37
- **Threads**: 4
- **Warmup**: 5 iterations, 10s each
- **Measurement**: 5 iterations, 10s each
- **Mode**: Throughput (ops/second)

## Results Summary

| Data Structure | Workload | Threads | Throughput (ops/s) | Error Margin |
|---------------|----------|---------|-------------------|--------------|
| ConcurrentTrie (Lock-Striped) | Pure Writes (Random) | 4 | 445,301 | ±57,445 |
| ConcurrentTrie (Lock-Striped) | 70% Reads / 30% Writes (Hot Prefixes) | 4 | 715,975 | ±105,160 |
| ConcurrentTrie (Lock-Striped) | 80% Writes / 20% Reads (Hot Prefixes) | 4 | 514,577 | ±47,218 |
| ConcurrentTrie (Lock-Striped) | 100% Writes (Random) | 4 | 467,337 | ±13,699 |
| ConcurrentTrie (Lock-Striped) | 100% Writes (Random) | 8 | 447,653 | ±31,771 |
| SynchronizedTrie (Global Lock) | 70% Reads / 30% Writes (Hot Prefixes) | 4 | 666,748 | ±54,592 |
| SynchronizedTrie (Global Lock) | 80% Writes / 20% Reads (Hot Prefixes) | 4 | 500,924 | ±40,664 |
| SynchronizedTrie (Global Lock) | 100% Writes (Random) | 4 | 407,337 | ±35,620 |
| SynchronizedTrie (Global Lock) | 100% Writes (Random) | 8 | 381,210 | ±36,682 |

## ConcurrentTrie Performance (Lock-Striped Locking)

### Pure Write Workload
- **Average**: 445,301 ops/s
- **Per-thread**: ~111k ops/s
- **Operations**: 100% adds with random strings (3-10 characters)
- **Locking Strategy**: Per-character `ReadWriteLock`

### Mixed Workload (70% Read / 30% Write)
- **Average**: 715,975 ops/s
- **Per-thread**: ~179k ops/s
- **Operations**: 70% `containsExact()`, 30% `add()`
- **Word Distribution**: 70% hot prefixes (`aa`, `ab`, `ac`, `ad`, `ae`), 30% random
- **Performance**: **60% faster** than pure writes due to concurrent reads

### Key Design Choices
- `ConcurrentHashMap` for lock map (no contention on lock acquisition)
- Lock granularity at first character level
- `ReadWriteLock` allows concurrent reads

### Performance Characteristics
- Read-heavy workloads benefit significantly from concurrent reads
- Lock contention minimized by spreading operations across alphabet
- Higher variance with hot prefixes (±105k vs ±57k for pure writes)

## SynchronizedTrie Performance (Global Lock)

### Mixed Workload (70% Read / 30% Write)
- **Average**: 666,748 ops/s
- **Per-thread**: ~167k ops/s
- **Operations**: 70% `containsExact()`, 30% `add()`
- **Word Distribution**: 70% hot prefixes (`aa`, `ab`, `ac`, `ad`, `ae`), 30% random
- **Locking Strategy**: Single global `ReadWriteLock` for entire trie

### Key Design Choices
- One `ReadWriteLock` for all operations
- Simpler implementation - no lock management overhead
- All writes completely serialize

### Performance Characteristics
- **Only 7% slower** than Lock-Striped locking for read-heavy workloads
- **Lower variance**: ±54k vs ±105k (more predictable performance)
- Multiple readers can still run concurrently
- Simpler code with less memory overhead
- Trade-off: Simplicity vs slight performance loss

## Comparison: Lock-Striped vs Global Lock

### Read-Heavy Workload (70% Reads / 30% Writes, Hot Prefixes, 4 threads)

| Metric | Lock-Striped | Global Lock | Winner |
|--------|-------------|-------------|---------|
| Throughput | 715,975 ops/s | 666,748 ops/s | Lock-Striped (+7.4%) |
| Variance | ±105,160 | ±54,592 | Global (more stable) |
| Code Complexity | High | Low | Global |
| Memory Overhead | High (lock map) | Low (one lock) | Global |

**Key Insight:** For read-heavy workloads with hot prefixes, global lock is competitive. The 7.4% performance advantage of Lock-Striped locking may not justify the added complexity.

---

### Write-Heavy Workload (80% Writes / 20% Reads, Hot Prefixes, 4 threads)

| Metric | Lock-Striped | Global Lock | Winner |
|--------|-------------|-------------|---------|
| Throughput | 514,577 ops/s | 500,924 ops/s | Lock-Striped (+2.7%) |
| Variance | ±47,218 | ±40,664 | Global (slightly more stable) |

**Key Insight:** With hot prefixes and write-heavy workload, both implementations perform nearly identically (~2.7% difference). Lock-Striped locking degrades to behaving like a global lock since most operations contend for the same lock (character 'a').

---

### Pure Write Workload (100% Writes, Evenly Distributed, 4 threads)

| Metric | Lock-Striped | Global Lock | Winner |
|--------|-------------|-------------|---------|
| Throughput | 467,337 ops/s | 407,337 ops/s | Lock-Striped (+14.7%) |
| Variance | ±13,699 | ±35,620 | Lock-Striped (more stable) |

**Key Insight:** This is where Lock-Striped locking shines! With evenly distributed writes across the alphabet, threads can modify different parts of the trie concurrently. Lock-Striped wins by 14.7% with better stability.

---

### Pure Write Workload (100% Writes, Evenly Distributed, 8 threads)

| Metric | Lock-Striped | Global Lock | Winner |
|--------|-------------|-------------|---------|
| Throughput | 447,653 ops/s | 381,210 ops/s | Lock-Striped (+17.4%) |
| Variance | ±31,771 | ±36,682 | Lock-Striped (slightly more stable) |

**Key Insight:** The gap widens with more threads! Global lock performance **degraded** from 407k (4 threads) to 381k (8 threads), while Lock-Striped stayed relatively stable (467k → 448k). This demonstrates the scalability advantage of Lock-Striped locking.

---

### Scaling Analysis

**Global Lock Scaling (100% Writes, Random):**
- 4 threads: 407,337 ops/s
- 8 threads: 381,210 ops/s
- **Change: -6.4%** (performance degrades)

**Lock-Striped Lock Scaling (100% Writes, Random):**
- 4 threads: 467,337 ops/s
- 8 threads: 447,653 ops/s
- **Change: -4.2%** (more graceful degradation)

**Why?**
- **Global lock:** All threads serialize through one lock. More threads = more contention, context switches, and waiting. Performance degrades significantly.
- **Lock-Striped lock:** Threads work independently on different prefixes. Some contention exists but doesn't worsen as dramatically with more threads.

---

### Summary: When to Use Each Approach

**Use Global Lock when:**
- Read-heavy workload (>70% reads)
- Hot prefix access patterns (most operations hit same locks anyway)
- Code simplicity is prioritized over peak performance
- Thread count is low (≤4 threads)

**Use Lock-Striped Lock when:**
- Write-heavy workload (>50% writes)
- Evenly distributed access patterns across prefixes
- High thread count (≥8 threads)
- Maximum throughput and scalability are critical
- Willing to accept implementation complexity

**Performance differences:**
- Read-heavy: Global lock ~7% slower (acceptable trade-off for simplicity)
- Write-heavy with hot prefixes: Equivalent (~3% difference)
- Write-heavy with distributed access: Lock-Striped 15-17% faster and scales better
- **Average**: 19,229 ops/s
- **Per-thread**: ~4.8k ops/s
- **Operations**: Send/receive with rendezvous synchronization
- **Locking Strategy**: Blocking coordination between senders/receivers

## Notes
- Trie performance benefits from mostly independent operations
- Both implementations show expected behavior for their synchronization models
- Confidence intervals assume normal distribution (99.9%)

## Future Optimizations
### ConcurrentTrie (Lock-Striped)
- Lock-free reads for `containsExact()`
- Reduce string allocations
- Lock striping within character subtrees
- Test write-heavy workloads to validate scalability advantage

### SynchronizedTrie (Global Lock)
- Already optimized for simplicity
- Performance adequate for read-heavy use cases
- Consider this approach when code simplicity matters more than peak throughput
