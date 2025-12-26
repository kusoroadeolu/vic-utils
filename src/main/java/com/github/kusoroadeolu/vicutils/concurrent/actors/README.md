

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