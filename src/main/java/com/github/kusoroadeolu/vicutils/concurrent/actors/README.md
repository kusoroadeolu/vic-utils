

- Actors can have no, one or multiple addresses
- Other actors must know the address of the actor they want to send the message to
- Actors use asynchronous message passing to send messages to other actor mailboxes. No intermediate entities like channels. However, actor mailboxes can be channels
- Actors encapsulate their thread + state
## Source Material
Actors are objects which confine their mutable state. They modify their mutable state based on messages they receive and what behavior they exert based on that message.

### π Calculus and actors
A number of formal models were proposed to formalize the fundamental nature of concurrent computation dealing with mobility and interaction. Of these models, the one that relates with the actor model was the π calculus
</br> The π calculus was evolved from a model of concurrency called **CCS**. Channels in **CCS** are interconnected by a static topology, meaning channels could not be informed of how to connect to other channels once connected.
</br> To overcome such limitations, the π calculus was brought forward. It allowed channels to be interconnected through a dynamic topology, which allowed channels to pass the names of other channels as data allowing them to create new connections with other channels.
</br> However with these similarities, they still had major differences
- The major difference was that π calculus was designed to model stateless channels meanwhile actors were designed to be stateful channels. Researchers did try adding a type system to π calculus, but it was unfruitful because π calculus did not account for unique actor identities
- Another difference was message passing through π calculus channels are designed to be synchronous while message passing through actors are asynchronous
- Message delivery in the actor model is also fair which allows for greater modularity in reasoning.


### The concrete functions of this actor lib
send(a; v) creates a new message:
- with receiver a, and
- contents v
newactor(b) creates a new actor:
- with behavior b, and
- returns its address
ready(b) captures local state change:
- replaces the behavior of the executing actor with b
- frees the actor to accept another message.

# VicUtils Actor System

A lightweight actor model implementation for Java, built on virtual threads for high-concurrency applications.

## What is it?

This library implements the actor model – a way to handle concurrency where isolated "actors" communicate through asynchronous message passing instead of sharing state. Think of each actor as its own little worker that processes messages one at a time from its mailbox.

## Key Features

- **Message-based concurrency** – Actors communicate by sending messages, no shared mutable state
- **Virtual thread powered** – Uses Java's virtual threads for lightweight, scalable concurrency
- **Supervision hierarchies** – Parent actors automatically restart failed children
- **Type-safe messaging** – Compile-time checking for message types
- **Behavioral state management** – Actors can change behavior based on messages received

## Quick Start

```java
// Create the actor system
ActorSystem system = ActorSystem.getContext();

// Define your actor
public class Counter extends AbstractActor<Message> {
    private int count = 0;
    
    public Counter(Behaviour<Message> behaviour) {
        super(behaviour);
    }
    
    @Override
    public MessageHandler<Message> handleMessages() {
        return MessageHandler.<Message>builder()
            .onMessage(Increment.class, msg -> {
                count++;
                return Behaviour.same(); // Keep current behavior
            })
            .build();
    }
}

// Create and use the actor
ActorRef<Message> counter = system.createActor(Counter::new);
counter.tell(new Increment(1));
```

## Core Concepts

### Actors
Self-contained units that:
- Process messages sequentially from their mailbox
- Maintain their own internal state (thread-safe by design)
- Can spawn child actors
- Can change behavior in response to messages

### Messages
Simple data objects that actors send to each other:
```java
public record Increment(int amount) implements Message {}
```

### Behaviors
Define how actors respond to messages:
- `Behaviour.same()` – Keep current behavior
- `Behaviour.sink()` – Ignore all messages
- Custom behaviors – Return new behavior to change state

### Supervision
When an actor fails, its parent automatically:
- Stops the failed actor
- Recreates it with the same address
- Restarts all its children
- Maintains system stability

## When to Use This

**Good for:**
- High-concurrency applications with isolated state
- Systems that need fault tolerance and automatic recovery
- Event-driven architectures
- Avoiding traditional lock-based concurrency

**Maybe not for:**
- Simple sequential programs
- When you need synchronous request-response (actors are async)
- Very low-latency requirements (message passing has overhead)

## Architecture

```
ActorSystem (root)
    └── Your actors
        └── Child actors
            └── More children...
```

Each actor runs on its own virtual thread, processing messages from its mailbox one at a time. No locks, no shared state – just messages flowing through the system.

## Lifecycle Hooks

Override these to add custom behavior:

```java
public void preStart()        // Before actor starts processing
public void preStop()         // Before actor stops
public void onChildRestart()  // When a child actor restarts
```

## Thread Safety

Actors are thread-safe by design. Each actor:
- Processes one message at a time
- Has exclusive access to its own state
- Communicates only through message passing

No manual synchronization needed!

## Inspiration

Based on the actor model formalized in π-calculus, with influences from Erlang/OTP and Akka.

---

Built with ☕ and virtual threads
