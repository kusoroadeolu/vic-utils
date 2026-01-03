# Benchmark Results

## Test Environment
- **JVM**: Java HotSpot 64-Bit Server VM, JDK 25+37-LTS-3491
- **JMH Version**: 1.37
- **Threads**: 4
- **Warmup**: 5 iterations, 10s each
- **Measurement**: 5 iterations, 10s each
- **Mode**: Throughput (ops/second)

## Results Summary

| Data Structure | Throughput (ops/s) | Error Margin | Min | Max |
|---------------|-------------------|--------------|-----|-----|
| ConcurrentTrie | 445,301 | ±57,445 | 429,426 | 476,647 |


## ConcurrentTrie Performance
- **Average**: 445,301 ops/s
- **Per-thread**: ~111k ops/s
- **Operations**: Add random strings (3-10 characters)
- **Locking Strategy**: Fine-grained per-character locking using `ReadWriteLock`

### Key Design Choices
- `ConcurrentHashMap` for lock map (no contention on lock acquisition)
- Lock granularity at first character level
- `ReadWriteLock` allows concurrent reads

### Performance Characteristics
- Variance: 429k - 476k ops/s (±13%)
- Lock contention minimized by spreading operations across alphabet


## Notes
- Trie performance benefits from mostly independent operations
- Confidence intervals assume normal distribution (99.9%)


