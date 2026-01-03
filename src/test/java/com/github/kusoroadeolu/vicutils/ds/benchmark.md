# Benchmark Results

## Test Environment
- **JVM**: Java HotSpot 64-Bit Server VM, JDK 25+37-LTS-3491
- **JMH Version**: 1.37
- **Threads**: 4
- **Warmup**: 5 iterations, 10s each
- **Measurement**: 5 iterations, 10s each
- **Mode**: Throughput (ops/second)

## Results Summary

| Data Structure | Workload | Throughput (ops/s) | Error Margin | Min | Max |
|---------------|----------|-------------------|--------------|-----|-----|
| ConcurrentTrie (Fine-grained) | Pure Writes | 445,301 | ±57,445 | 429,426 | 476,647 |
| ConcurrentTrie (Fine-grained) | 70% Reads / 30% Writes | 715,975 | ±105,160 | 575,037 | 744,603 |
| SynchronizedTrie (Global Lock) | 70% Reads / 30% Writes | 666,748 | ±54,592 | 580,758 | 678,748 |

## ConcurrentTrie Performance (Fine-grained Locking)

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
- **Only 7% slower** than fine-grained locking for read-heavy workloads
- **Lower variance**: ±54k vs ±105k (more predictable performance)
- Multiple readers can still run concurrently
- Simpler code with less memory overhead
- Trade-off: Simplicity vs slight performance loss

## Comparison: Fine-grained vs Global Lock

| Metric | Fine-grained | Global Lock | Winner |
|--------|-------------|-------------|---------|
| Throughput (70% reads) | 715,975 ops/s | 666,748 ops/s | Fine-grained (+7%) |
| Variance | ±105,160 | ±54,592 | Global (more stable) |
| Code Complexity | High | Low | Global |
| Memory Overhead | High (lock map) | Low (one lock) | Global |
| Scalability Potential | Better | Limited | Fine-grained |

### Key Insights
- For **read-heavy workloads** (70%+ reads), global lock is competitive and much simpler
- Fine-grained locking's 7% advantage may not justify the added complexity
- Both implementations benefit from `ReadWriteLock` allowing concurrent reads
- Global lock shows more consistent performance (lower variance)
- **Prediction**: Fine-grained locking will pull ahead significantly in write-heavy scenarios
- **Average**: 19,229 ops/s
- **Per-thread**: ~4.8k ops/s
- **Operations**: Send/receive with rendezvous synchronization
- **Locking Strategy**: Blocking coordination between senders/receivers

## Notes
- Trie performance benefits from mostly independent operations
- Channel performance limited by required coordination between threads
- Both implementations show expected behavior for their synchronization models
- Confidence intervals assume normal distribution (99.9%)

## Future Optimizations
### ConcurrentTrie (Fine-grained)
- Lock-free reads for `containsExact()`
- Reduce string allocations
- Lock striping within character subtrees
- Test write-heavy workloads to validate scalability advantage

### SynchronizedTrie (Global Lock)
- Already optimized for simplicity
- Performance adequate for read-heavy use cases
- Consider this approach when code simplicity matters more than peak throughput
