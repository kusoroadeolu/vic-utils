# vic-utils:  Deep Learning in Concurrency

An experimental, educational collection of **from-scratch concurrency primitives** and supporting data structures in Java.  Built to explore, understand, and benchmark concurrent programming concepts—no production aims, pure learning.

---

## Table of Contents

1. [Philosophy](#philosophy)
2. [Project Architecture](#project-architecture)
3. [Deep Dive:  Channels (CSP)](#deep-dive-channels-csp)
4. [Deep Dive: Actors](#deep-dive-actors)
5. [Deep Dive: Synchronization Primitives](#deep-dive-synchronization-primitives)
6. [Deep Dive: Optimistic Entity Pattern](#deep-dive-optimistic-entity-pattern)
7. [Deep Dive: Data Structures](#deep-dive-data-structures)
8. [Benchmarking & Performance Analysis](#benchmarking--performance-analysis)
9. [Key Insights & Lessons Learned](#key-insights--lessons-learned)

---

## Philosophy

**What you won't find:**
- Production-ready code
- Third-party concurrency libraries
- Performance-optimized implementations

**What you will find:**
- **Transparent designs** with clear invariants and trade-offs
- **Detailed comments** explaining the *why*, not just the *what*
- **Real bugs and lessons** (deadlock issues, fairness problems, reference equality gotchas)
- **Benchmarks** showing relative performance of different synchronization strategies
- **Theory and practice** merged together

---

## Project Architecture

```
vic-utils/
├── src/main/java/com/github/kusoroadeolu/vicutils/
│   ├── concurrent/
│   │   ├── channels/           # CSP-style synchronization
│   │   ├── actors/             # Message-passing concurrency model
│   │   ├── mutex/              # Custom mutual exclusion lock
│   │   ├── semaphore/          # Channel-backed semaphore
│   │   └── optimistic/         # Optimistic entity pattern
│   ├── ds/                     # Data structures
│   │   ├── ShitSkipList.java   # Probabilistic skip list
│   │   ├── SequentialTrie.java # Single-threaded trie
│   │   ├── ConcurrentTrie. java # Fine-grained locked trie
│   │   └── ConcurrentLRUCache.java
│   └── misc/                   # Utilities (Try, LockHelper)
└── src/test/
    └── benchmarks/             # JMH microbenchmarks
```

---

# Deep Dive: Channels (CSP)

## Overview

**Channels** implement **Communication Sequential Processes (CSP)**—a paradigm where threads communicate by *sending messages through channels* rather than *sharing memory*.

### Fundamental Invariant
```
Channel State: NIL → OPEN → CLOSED (one-way progression)
```

### Channel Types

| Implementation | Mechanism | Use Case | Throughput | Latency | CPU |
|---|---|---|---|---|---|
| **UnBufferedChannel** | ReentrantLock + Conditions | Simple synchronization | ~100K op/s | Higher | Lower |
| **RendezvousChannel** | Lock-based conditions with reference wrapping | CSP semantics + fairness | ~100K op/s | Stable | Efficient |
| **SpinRendezvousChannel** | Lock-free (CAS) + spin-waiting | Peak throughput | ~5M op/s | Variable | Higher |
| **BufferedChannel** | UnBuffered + bounded queue | Async communication | ~500K op/s | Medium | Medium |

---

## RendezvousChannel: The Reference Equality Deadlock Lesson

This is perhaps the most educational implementation. It demonstrates a **subtle concurrency bug** that reveals deep truths about reference equality and synchronization.

### The Bug:  String Literal Interning Deadlock

**Problem Scenario:**
```java
channel.send("msg");  // Producer A
channel.send("msg");  // Producer B
channel.receive();    // Consumer
```

Java interns string literals—meaning all `"msg"` references point to the **same object in memory**.

**Deadlock Scenario:**
```
1. Producer A:    send("msg") → wraps in Box A
2. Producer A:   waits on itemConsumed (checking:  t == boxA ?)
3. Consumer:     takes value, sets t = null, signals itemConsumed
4. Producer B:   sends "msg" → wraps in Box B (different Box, same "msg")
5. Producer B:   sets t = boxB
6. Producer A:   wakes up, checks t == boxA → FALSE (t is boxB now)
7. Producer A:   wakes fully, BUT... 
8. Producer B:   also woke up checking (t == boxB ?)
9. Producer A:   retries CAS, sets t = boxA
10. Producer B:  wakes, checks t == boxB → FALSE
11. Both spin waiting on itemConsumed with only ONE signal coming → DEADLOCK
```

### The Fix: Box Wrapper Record

```java
record Box<T>(T value) { }

// In send():
Box<T> b = new Box<>(val);  // Unique object per send! 
t = b;
while (t == b && !this.isClosed()) { // Reference equality works now
    itemConsumed.awaitUninterruptibly();
}
```

**Key Insight:** When using reference equality (`==`) for synchronization, ensure unique objects.  Wrapping values in a record guarantees uniqueness.

---

## RendezvousChannel: Synchronization Conditions

Three conditions manage the rendezvous:

```java
private final Condition isFull;        // Producers wait here if t != null
private final Condition isEmpty;       // Consumers wait here if t == null
private final Condition itemConsumed;  // Producers wait for consumption
```

### Send Flow:
```
1. Wrap value in Box (unique reference)
2. Lock
3. Wait while t != null (isFull condition)
4. Set t = wrapped_value
5. Signal isEmpty (wake consumer)
6. Wait while t == my_box (itemConsumed condition) — consumer must consume
7. Unlock
```

### Receive Flow:
```
1. Lock
2. Wait while t == null (isEmpty condition)
3. Extract value
4. Set t = null (CRITICAL: receiver invalidates the condition)
5. Signal itemConsumed (wake waiting producer)
6. Unlock
```

**Critical Rule:** *The thread that waits on a condition must be the one to invalidate it. * This is why the **consumer** sets `t = null`, not the producer.

---

## SpinRendezvousChannel: Lock-Free Variant

Replaces locks with **Compare-And-Swap (CAS)** loops and **spin-waiting**.

### Send Implementation:
```java
public void send(T val) {
    // First loop: Wait until ref is null, then atomically set it
    while (! ref.compareAndSet(null, val)) {
        if (this.isClosed()) throw new ChannelClosedException(... );
        Thread.onSpinWait(); // Spin, don't block
    }
    
    // Second loop: Wait until our value is consumed (ref != val)
    while (ref.get() == val && !this.isClosed()) {
        Thread.onSpinWait();
    }
}
```

### Receive Implementation:
```java
public Optional<T> receive() {
    while (! this.isClosed()) {
        T val = ref.get();
        // Atomically get AND clear
        if (val != null && ref.compareAndSet(val, null)) {
            return Optional.of(val);
        }
        Thread.onSpinWait();
    }
}
```

### Trade-offs:
- **Throughput:** 50x faster (~5M vs 100K ops/s)
- **CPU:** Burns CPU during spin-waiting
- **Variance:** Higher variability in latency (~12% vs ~2. 5% for lock-based)
- **No lock contention:** Works well when contention is low

**When to use:**
- 1P1C (one producer, one consumer) scenarios
- Microsecond-level latency critical
- CPU budget available
- Workloads where waiting is brief

---

## Channel Variance Analysis:  4P4C Scenario

When contention increases (4 producers, 4 consumers), lock-free becomes less predictable:

| Implementation | Mean (ops/s) | Coefficient of Variation | Absolute Variance |
|---|---|---|---|
| **SpinRendezvousChannel** | 6. 8M | ~12% | ±827K | ← High variance
| **SynchronousQueue** | 4.6M | ~8% | ±367K | ← Stable
| **RendezvousChannel** | 23K | ~2.5% | ±576 | ← Most stable

**Key Insight:** Lock-free shines in low-contention scenarios. Under contention, predictability matters more than peak throughput.

---

# Deep Dive: Actors

## Overview

**Actors** are isolated, stateful components that communicate *exclusively via asynchronous messages*. Each actor:
- Owns its own thread (virtual thread)
- Has a mailbox (message queue)
- Processes one message at a time
- Can spawn child actors

### Actor Model Flow:
```
Actor 1                    Actor 2
   |                          |
   +-- tell(Message) -------->|
                             |
                        [Mailbox]
                             |
                        Process message
                             |
                          [Reply]
```

## Behavior Composition

Actors use **Behavior** to encode response logic:

```java
ActorSystem system = ActorSystem.getContext();

ActorRef<Message> counter = system.createActor(
    behaviour -> new CounterActor(behaviour)
);

// Behavior changes at runtime
actor.tell(new IncrementMessage());  // Behavior updates internally
actor.tell(new GetCountMessage());
```

## Actor Lifecycle & Supervision

```
Parent Actor
    |
    +-- spawn(Child1)
    |    |
    |    +-- sends:  ChildDeath(address, behavior, children)
    |         on exception → Parent handles restart
    |
    +-- spawn(Child2)
```

**Invariant:** Child death automatically notifies parent.  Parent can restart or propagate upward.

---

## Virtual Threads Integration

```java
private static final ExecutorService EXECUTOR = 
    Executors.newVirtualThreadPerTaskExecutor();
```

Each actor runs on a **virtual thread**, enabling millions of concurrent actors with minimal memory overhead.

---

# Deep Dive: Synchronization Primitives

## Mutex:  CAS-based Mutual Exclusion

### State Machine:
```
State Value | Meaning
     0      | Released (available)
     1      | Releasing (transitional)
     2      | Acquired (held)
```

### Key Fields:
```java
@State
private final AtomicReference<Integer> state;  // Main lock state
private final Queue<Thread> waiters;            // Fair waiter queue
private volatile Thread holder;                 // Reentrancy tracking
private volatile Thread next;                   // Next-in-line hint
private int acquires;                           // Reentrant count
```

### Acquire Algorithm:

```
1. If current thread == holder: 
     -> Increment reentrant count, return (reentrant)
   
2. Attempt:  compareAndSet(state, 0→2)
   Success -> Go to step 6
   Failure -> Continue to step 3

3. Add self to waiters queue
   
4. Spin wait for state != 1 (releasing)
   
5. If I'm the "next" thread hint, retry CAS
   Otherwise, park() and wait for unpark

6. Set holder = me, clear next, remove from queue
```

### Release Algorithm:

```
1. Verify: holder == me (else throw IllegalMonitorStateException)
   
2. If reentrant count > 0:
     -> Decrement and return (still held)
   
3. Set state = 1 (releasing)
   
4. Peek at waiters queue
   If not empty: set next = waiters.peek(), unpark(next)
   
5. Set state = 0 (released)
   
6. Set holder = null (volatile write ensures visibility)
```

### Memory Ordering Guarantees:

The volatile write to `holder` at release is critical:

```java
state.set(1);          // Mark as releasing
Thread next = waiters.peek();
if (next != null) {
    this.next = next;
    LockSupport.unpark(next);
}
state.set(0);
holder = null;  // ← VOLATILE WRITE
// Happens-before: Previous writes to shared data are visible to next acquirer
```

**Invariant:** By the time `holder` is cleared (volatile write), all shared data is flushed.

---

## Chamaphore: Channel Backed Semaphore

Why reinvent with channels?

```java
public class Chamaphore {
    private final Channel<Integer> channel;
    
    public Chamaphore(int allowed) {
        this.channel = new BufferedChannel<>(allowed);
        channel.make();
    }
    
    public void acquire() {
        var idx = parkedThreads.incrementAndGet();
        channel.send(idx);  // Block until permit available
        parkedThreads.decrementAndGet();
        acquiredPermits.incrementAndGet();
    }
    
    public int release() {
        var released = channel.tryReceive();
        if (released. isPresent()) acquiredPermits.decrementAndGet();
        return released.orElse(0);
    }
}
```

**Benefits:**
- CSP semantics for fairness (FIFO)
- Cleaner code (channel abstractions)
- Built on proven channel primitives
- Automatic blocking on send()

---

# Deep Dive: Optimistic Entity Pattern

## Overview

A **novel concurrency model** combining:
- Actor-like single-threaded processing
- Optimistic locking (version-based)
- Event sourcing (version history)

### Core Idea:
Instead of locking, clients submit **proposals** with **expected version numbers**. The entity accepts only if versions match:

```
Client                          Entity (Single-threaded)
  |                                |
  +-- Snapshot (get version)       |
  |    "I see version 5"          |
  |                                |
  +-- Propose(version=5, new_val) |
      |                            |
      +-- Check:  entity. version == 5? 
          YES  → Apply & increment version
          NO   → Reject silently (no retry)
```

---

## Proposal Structure

```java
public final class Proposal<E, T> implements Proposable<E> {
    private int versionNo;              // Expected version
    private T proposedValue;            // New value
    private BiFunction<E, T, E> setter; // How to apply change
    private Runnable onSuccess;
    private Runnable onReject;
}
```

### Building a Proposal:
```java
Entity<Document> doc = Entities.spawnEntity(new Document(1));
Document snapshot = doc.snapshot();

Proposal<Document, String> proposal = new Proposal<>()
    .builder()
    .versionNo((int) doc.versionNo())     // Capture expected version
    .setter(Document::setText)             // How to change
    .proposedValue("Updated text")         // New value
    .onSuccess(() -> System.out.println("Applied"))
    .onReject(() -> System.out.println("Stale"))
    .build();

doc.propose(proposal);
```

---

## Batch Proposals:  All-or-Nothing

```java
List<Proposal<Document, ? >> batch = List.of(
    proposal1.setter(Document::setTitle),
    proposal2.setter(Document::setText),
    proposal3.setter(Document::setAuthor)
);

BatchProposal<Document> batch = new BatchProposal<>()
    .versionNo(snapshot_version)
    .proposals(batch)
    .build();

doc.propose(batch);
// If ANY proposal has stale version → entire batch rejected
```

---

## OptimisticEntity: Processing Loop

```java
void start() {
    Thread. startVirtualThread(() -> {
        while (isRunning) {
            Proposable<E> proposable = queue.poll();
            
            if (proposable != null) {
                ++proposalsSubmitted;
                
                List<Proposal<E, ?>> proposals = switch (proposable) {
                    case Proposal<E, ?> p -> {
                        int versionNo = p.versionNo();
                        yield List.of(p);
                    }
                    case BatchProposal<E, ?> bp -> {
                        int versionNo = bp.versionNo();
                        yield bp.proposals();
                    }
                };
                
                processProposals(proposals, versionNo, 
                    proposable.onSuccess(), proposable.onReject());
            }
        }
    });
}

private void processProposals(List<Proposal<E, ?>> proposals, 
                               int versionNo,
                               Runnable onSuccess, Runnable onReject) {
    if (versionNo > this.versionNo) {
        // Version is stale
        rejectedProposals.add(proposals);
        ++rejectedCount;
        tryRun(onReject);
    } else {
        // Apply all proposals
        ++this.versionNo;
        for (Proposal<E, ?> p : proposals) {
            state = p.setter().apply(state, p.proposedValue());
        }
        versions.put(this.versionNo, state);  // Version history
        tryRun(onSuccess);
    }
}
```

---

## Invariants & Guarantees

```
1. Single-threaded processing
    At most one proposal processed at a time
   
2. Version monotonicity
    versionNo only increases
   
3. Stale rejection
    If proposal. versionNo < entity.versionNo → always reject
   
4. No retries
    Rejected proposals are dropped permanently
   
5. Batch atomicity
    ALL or NOTHING for batch proposals
   
6. History immutability
    Snapshots at past versions never change
```

---

## Metrics & Introspection

```java
long rejectedCount = entity.rejectedCount();
double rejectionRate = entity.rejectionRate();
List<List<Proposal>> rejected = entity.rejectedProposals();
E stateAtVersion5 = entity.snapshotAt(5);
long currentVersion = entity.versionNo();
```

---

## Use Cases

**When Optimistic Entities Shine:**

**Document editing** (Google Docs-like collab)
- Multiple clients submit edits
- Edits with stale versions rejected, requires re snapshot

**Inventory management**
- Concurrent stock updates
- Inventory changed?  Reject and retry

**Event sourcing**
- Version history built-in
- Append-only structure

 **High-concurrency reads, low contention writes**
- No blocking on proposal submission
- Single entity thread does all work

**Anti-pattern:**
High-contention scenarios (many stale rejections)
Clients need guaranteed success
Complex conditional logic (needs context from rejection)

---

# Deep Dive: Data Structures

## ShitSkipList<E>: Probabilistic Indexing

**Why the name?** The author chose it humorously—it's not production-ready, but it's *shitty-in-a-fun-way*.

### Structure:
```
Level 3: [1] ─────────────────────► [5]
Level 2: [1] ──────► [3] ──────► [5]
Level 1: [1] ► [2] ► [3] ► [4] ► [5]
```

Each element randomly promoted to higher levels with probability `p`.

### Operations:
```java
ShitSkipList<Integer> list = new ShitSkipList<>(0.5);
list.add(1); list.add(2); list.add(3);
boolean found = list.contains(2);       // O(log n) average
list.remove(2);
int first = list.first();
int last = list.last();
```

### Invariants:
- Elements are comparable (`Comparable<E>`)
- Layers organized lowest (0) to highest
- Higher layers have fewer elements
- Average search:  O(log n)

---

## SequentialTrie & ConcurrentTrie

### SequentialTrie:
```java
Trie trie = new SequentialTrie();
trie.add("hello");
trie.add("help");
trie.add("world");

List<String> withPrefix = trie.startsWith("hel"); // ["hello", "help"]
boolean hasWord = trie.containsExact("hello");     // true
boolean hasPrefix = trie.containsPrefix("hel");    // true (has words under prefix)
```

### ConcurrentTrie:
Fine-grained locking per-character:
```java
ConcurrentTrie trie = new ConcurrentTrie();
// Each first character ('h', 'w', etc.) has its own ReadWriteLock
trie.add("hello");      // Locks 'h'
trie.add("world");      // Locks 'w' (concurrent!)
trie.containsExact("hello");  // Read lock on 'h'
```

**Design Choice:** Avoids copy-on-write (too expensive for deep tries).

---

## ConcurrentLRUCache:  Segmented Design

Reduces lock contention by partitioning into segments:

```java
ConcurrentLRUCache<String, String> cache = new ConcurrentLRUCache<>(1000);

// Internally:  16 segments
// hash(key) % 16 determines segment
// Each segment has its own lock
```

**Benefit:** 16 threads can modify different keys concurrently (different segments).

---

# Benchmarking & Performance Analysis

All benchmarks use **JMH (Java Microbenchmark Harness)** for accurate measurements.

## 1P1C (One Producer, One Consumer)

```
SpinRendezvousChannel:   ~5. 0M ops/s  (±5%)
SynchronousQueue:       ~3.5M ops/s  (±29%)
RendezvousChannel:      ~100K ops/s  (±13%)
```

**Learning:** Lock-free dominates with zero contention.

---

## 4P4C (Four Producers, Four Consumers)

```
SpinRendezvousChannel:  ~6.8M ops/s  (±12%)  ← Variance increases
SynchronousQueue:       ~4.6M ops/s  (±8%)
RendezvousChannel:      ~23K ops/s   (±2. 5%) ← Most stable
```

**Learning:** Contention changes the trade-off.  SpinRendezvous is erratic; RendezvousChannel is predictable.

---

## Variance Analysis Principles

### Lock-Free Variance Sources:
- Cache coherency protocol delays
- CPU scheduler decisions
- Which threads pair up for handoff
- More **fundamental coordination complexity**

### Lock-Based Variance Sources:
- Thread wake-up latency
- Lock acquisition timing
- Minor JVM GC pauses

---

## Platform Threads vs Virtual Threads

```
Virtual Threads (Java 21+):
  - Lower memory per actor
  - More concurrent tasks (millions)
  - Slightly higher latency due to GC overhead
  
Platform Threads:
  - Higher memory per thread
  - Lower latency
  - Scalability limited (thousands)
```

---

# Key Insights & Lessons Learned

## 1. Reference Equality for Synchronization

**Lesson:** When using `==` for coordination, ensure unique objects.

```java
// String interning causes deadlock
while (t == "msg") { ... }

// Unique wrapper per send
Box<String> b = new Box<>("msg");
while (t == b) { ... }
```

---

## 2. Condition Invalidation Ownership

**Lesson:** The thread *waiting* on a condition must be the one to *invalidate* it.

```java
//  Producer sets to null
condition.awaitUninterruptibly();

//  Consumer sets to null (consumer wakes first)
itemConsumed. signal();
```

---

## 3. Volatile Writes as Happens-Before Edges

**Lesson:** Volatile writes create happens-before relationships.

```java
state. set(0);
holder = null;  // ← Volatile write
// All previous writes to shared data are now visible to next thread
```

---

## 4. Lock-Free Wins at Scale, Loses at Variance

**Lesson:** CAS-based synchronization maximizes throughput but sacrifices predictability.

```
1P1C:  SpinRendezvous (lock-free) wins
4P4C:   RendezvousChannel (lock-based) is more stable
```

Choose based on workload requirements, not just throughput.

---

## 5. Virtual Threads Change the Scaling Model

**Lesson:** Virtual threads enable millions of concurrent actors, changing architecture design.

```java
// Old: Limited to ~10K platform threads
// New: Millions of virtual threads feasible
```

This makes actor models more practical without heavyweight thread pools.

---

## 6. Optimistic Locking Requires Discipline

**Lesson:** Version-based optimistic locking is elegant but demands client understanding.

```java
// Client must: 
// 1. Take entity with version
// 2. Check for stale versions (rejection)

// No automatic retry = no hidden blocking
// But also no magic = race conditions visible
```

---

## 7. Trade-offs Are Fundamental

Every implementation embodies a **triangle of properties:**

```
       Throughput
       /        \
    Latency    Variance
      |          |
      +-- Fairness
         (Ordering)
```

- **SpinRendezvous:** High throughput, high variance, unfair
- **RendezvousChannel:** Medium throughput, low variance, fair
- **SynchronousQueue:** High throughput (proven), moderate variance, moderate fairness

No single winner but context determines choice.

---

## 8. Java Memory Model is Subtle

**Lesson:** Understanding `happens-before` relationships is essential for correctness.

```java
// Without volatile: 
holder = null;  // Not visible to other threads immediately

// With volatile:
holder = null;  // Immediately visible, flushes prior writes
```

---

# Getting Started

## Requirements
- Java 25+ (for virtual threads and records)
- Maven 3.8+

## Build
```bash
mvn clean package
```

## Run Tests
```bash
mvn test
```

## Run Benchmarks
```bash
mvn test
# Results in:  src/test/java/com/github/kusoroadeolu/vicutils/concurrent/channels/*. md
```

---

# Final Thoughts

**vic-utils** is my **playground for understanding**. Each component teaches:

- **Channels:** CSP, condition variables, reference equality
- **Actors:** Message-passing, supervision, virtual threads
- **Mutex:** CAS semantics, fairness, memory ordering
- **OptimisticEntity:** Versioning, event sourcing, no-retry patterns
- **Data Structures:** Probabilistic balancing, fine-grained locking


---

## License

MIT 

