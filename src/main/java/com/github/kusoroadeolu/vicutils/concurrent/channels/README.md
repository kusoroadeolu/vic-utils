# Channels
This project is a port of Go's channels to Java, providing a CSP-style concurrency primitive for coordinating between threads. Built this mainly to
level up my concurrency skills and because Go's channels looked fun to implement.

## CSP(Communication Sequential Processes)
A channel follows CSP(Communication Sequential Processes), a concurrency style where sequential process communicate by sending messages through **channels** rather than sharing memory

## What is a channel
A channel is a synchronization primitive for sending data between processes

## Sharing memory
Java passes object references by value, but with important nuances
</br> Given we have an object `User`
```java
class User{
    String name;
    String password;
    //Getters, Setters...
}

void main(){
    var user = new User();
    modifyUser(user);
}

void modifyUser(User user){
    user.setPassword(...some password) //This modifies the user because it is still pointing the original reference
    user = new User(user.getName(), user.getPassword()) //This doesn't modify the user but rather creates a new object which points to a different reference
}
```
When you pass mutable objects around through methods or potentially **channels**, you're passing references to the same object in memory, so multiple threads can access the data.
</br> This creates problems regarding object state and mutability, and this is where **CSP** comes into play. 

## How CSP fixes this issue 
**CSP** states that threads/process communicate by sending messages/data rather than sharing **memory**. 
</br> Rather than passing references of objects around in memory to deliver them to other processes, **CSP** encourages we create new copies of that object, therefore a new reference and give it to a separate process.
</br> This negates all issues regarding mutability of the original object, though, there's still another nuance, we need to ensure this new object copy is safely published to another process, and this is where **channels** come in.
</br> **Note**: Like Go's channels, this implementation passes references (not copies) for performance. It's your responsibility to treat objects as immutable after sending them through a channel. For true isolation, use immutable types (records, sealed classes) or create defensive copies before sending.

```java
Channel<String> channel = new UnBufferedChannel<>();
channel.make();

// Producer thread
Thread.startVirtualThread(() -> channel.send("Hello!"));

// Consumer thread
Thread.startVirtualThread(() -> {
    String msg = channel.receive().orElse("empty");
    System.out.println(msg);
});
```
## Channels vs BlockingQueue
While Java's `BlockingQueue` provides similar blocking send/receive operations,
channels offer:
- Channel selectors for multiplexing across multiple channels
- Clear lifecycle management with nil/open/closed states
- Unidirectional channel types for API clarity
- More Go like semantics for those familiar with that model

## When channels shine
- Channels model intent - Who is allowed to send? Who is allowed to receive? What happens when it fails?
- Predictable backpressure
- Lifecycle clarity - Producers know when to stop sending, consumers know when to stop receiving. Pipelines can shut down cleanly
- Channels push you towards thread safe, message passing not shared memory


## Channel Types
A channel can only transfer values of the generic type of the channel.
</br> Channel types can be unidirectional or bidirectional. Assuming `T` is the channel type
- `Channel<T> chan` denotes a bidirectional channel
- `SendChannel<T> sc` denotes a unidirectional send-only channel
- `ReceiveChannel<T> sc` denotes a unidirectional receive-only channel
</br> Values of the bidirectional channel `Channel<T> chan` can be implicitly converted to both send-only type `chan.makeSendChannel()` and receive-only type `chan.makeReceiveChannel()`
</br> Rather than null, the zero value of channels are represent as `NIL`. Channels can be opened using the `make()` function or by making a unidirectional channel from a bidirectional channel

**Note:** Unidirectional channels made from bidirectional channels share the same state and reference as the bidirectional channel

## Channel capacity   
- Buffered channel -> Buffered channels decouple senders and receivers up to a fixed capacity.
</br> If the buffer is full, the producer blocks till space is freed up and if the buffer is empty, the consumer blocks until a producers sends a message
- Unbuffered channel -> A blocking channel which blocks producers if a receiver hasn't picked up their message and receivers if a producer has not sent a message

## Channel Operations
- `close()` - Closes a channel
- `send()` - Sends a value to a channel. Must be a send-only channel
- `trySend()` - Non blocking send
- `tryReceive()` - Non blocking receive
- `receive()` - Receives a value from a channel. Must be a receive-only channel
- `capacity()` - Query the buffer capacity of the channel i.e. The amount of objects the channel can hold
- `length()` - Query the current number of objects in the channel buffer
- `ok()` - Query if the channel is open
- `makeSendChannel()` - Creates a send-only channel
- `makeReceiveChannel()` - Creates a receive-only channel
- `isEmpty()` - Query if the channel is empty or not

## Channel Guarantees
To make the explanations for channel operations simple and clear, channels will be classified into three categories:
- Nil channels.
- Closed channels.
- Open channels.

| Operation     |          A Nil channel          | A Closed channel |     A Open channel |
|:--------------|:-------------------------------:|:-----------------|-------------------:|
| Send Value    | Blocks till the channel is made | Throws exception | Blocks or succeeds |
| Receive Value | Blocks till the channel is made | Never blocks     | Blocks or succeeds |
| Close         |        Throws exception         | Throws exception |  Succeeds to close |

- Closing a nil or an already closed channel throws an exception in the current thread.
- Sending a value to a closed channel also throws an exception in the current thread.
- Sending a value to or receiving a value from a nil channel makes the current thread enter and stay in blocking state till the channel is made

Each channel holds its own lock which it uses to avoid data races, maintain its state, block threads and safely publish objects

### When a thread tries to send a value to an open channel
Once the thread has acquired the channel's lock,
- If the buffer of the channel is empty and there is already a receiver waiting for the value, the value will be transferred directly to the receiver.
- If the buffer of the channel is empty and there is no receiver waiting for the value, the value will be moved into the buffer and the thread will wait till the buffer is emptied. This is a blocking operation
- If the buffer of the channel is not empty, the thread will wait till the buffer is emptied. This is a blocking operation
</br> The thread then releases the lock.

### When a thread tries to receive a value from an open channel
Once the thread has acquired the channel's lock,
- If the buffer of the channel is empty, the thread will wait until a value is pushed into the buffer. This operation is blocking
- If the buffer of the channel is not empty, the thread will pull the value out of the buffer. This operation is non-blocking
</br> The thread then releases the lock.

### When a thread tries to close an open channel
Once the thread has acquired the channel's lock,
- If the channel is `nil`, the thread throws an exception
- If the channel has already been closed, the thread throws an exception
- If the channel has not been closed, the thread sets the channel state to `CLOSED`
- **Note:** Receiving channels can still drain the buffer of a non-empty closed channel. Receives on closed channels are non-blocking
</br> The thread then releases the lock.


## Why NIL channels
Nil channels provide for more fine-grained control when you don't want channels to be able to send or receive yet but want the channel to exist, until you make it.


## Channel Selector
The `ChannelSelector` allows you to wait on multiple channels simultaneously, similar to Go's `select` statement. It will execute the first channel that becomes ready.
</br> While the `select` statement in Go is baked into the language's syntax, Java does not have such privilege. I however tried to make the API for the select statement as intuitive as possible


### Guarantees
- If all channels passed are nil, the selector will block forever
- If any channel is nil, the selector will spawn a virtual thread that blocks
  waiting for that channel to be made. While virtual threads are lightweight,
  passing nil channels to selectors is not recommended.
- If the selected channel's consumer throws an exception, that exception will always be propagated back to the calling thread


```java
Channel<Integer> chan1 = new UnBufferedChannel<>();
Channel<Integer> chan2 = new UnBufferedChannel<>();
Channel<Integer> chan3 = new UnBufferedChannel<>();
chan1.make(); chan2.make(); chan3.make();

// Send to channels from different threads
Thread.startVirtualThread(() -> chan1.send(1));
Thread.startVirtualThread(() -> chan2.send(2));
Thread.startVirtualThread(() -> chan3.send(3));

// Select waits for the first available value
Integer result = ChannelSelector.select(chan1, chan2, chan3)
    .onReceive(chan1, val -> System.out.println("Got from chan1: " + val))
    .onReceive(chan2, val -> System.out.println("Got from chan2: " + val))
    .onReceive(chan3, val -> System.out.println("Got from chan3: " + val))
    .execute();
```

### Selector Features
- **Timeout**: Set a timeout to avoid blocking forever
```java
Integer result = ChannelSelector.select(chan1, chan2)
    .timeout(1000) // milliseconds
    .execute();
```

- **Default value**: Provide a fallback if no channel is ready
```java
Integer result = ChannelSelector.select(chan1, chan2)
    .defaultTo(42)
    .execute();
```

**Note**: Only one channel's consumer will execute, even if multiple channels have values ready.

## REFERENCES FOR THIS IMPLEMENTATION
- [https://go101.org/article/channel.html](https://go101.org/article/channel.html)
