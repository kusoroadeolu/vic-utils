# Some notes about Java's Memory Model
## Happens before guarantees
### Program order happens before guarantee
This means that, operations in a thread are viewed exactly as it was ordered/written from that thread. Now this brings about the question of memory reordering, I initially thought that memory reordering was that the CPU can reorder your operations as long as the next doesn't depend on the previous,
</br> but this is only true, in a sense, for multithreaded systems. Memory reordering is more about visibility, how a thread sees the order of operations, which another thread carried out.
</br> **For Example**
```java
class SomeClass{
    int x = 0;
    int y = 0;
    
    //Thread 1 writes to x and y
    void write(){
        ++x;
        ++y;
    }
    
    //Thread 2 reads from a variable
    void read(){
        IO.println(x);
        IO.println(y);
    }
}
```
Ideally in an all perfect world, a thread 1 would ideally write to x and y before thread 2 reads, but this is where memory reordering comes into play. `Thread 1` will view its operations in the order the occurred due to the **happens before** guarantee, 
</br> but `Thread 2` might see that only `y` has been written, read both values for `x` and `y` before seeing a write to `x`. To prevent this proper synchronization of is needed.


```java
class SomeClass{
    int x = 0;
    int y = 0;
    
    //Thread 1 writes to x and y
    synchronized void write(){
        ++x;
        ++y;
    }
    
    //Thread 2 reads from a variable
    synchronized void read(){
        IO.println(x);
        IO.println(y);
    }
}
```
With these changes made, only one thread can hold this class's mutex at a time, therefore, `Thread 2` can only see the value of `x and y` after `Thread 1` has written or not, depending on which thread obtains the class's mutex first 

### Volatile happens-before guarantee
The JMM specification states that a write to a volatile field happens before subsequent reads to that field. The `volatile` keyword acts as a weaker synchronization primitive, it guarantees immediate visibility of writes to shared variables, by flushing writes to that variable immediately to main memory 
</br> to ensure other threads can immediately see the state of the variable. This however doesn't mean that if the reference to an object is volatile, writes and reads to its non-volatile fields will be guaranteed visibility as well.
```java
class Foo{
    private int count;
    //Getters Setters
}

class SomeClass{
    private volatile Foo foo; //Volatile ensures guaranteed visibility of writes to foo's reference
    
    void updateFoo(Foo foo){
        this.foo = foo;
    }
    
    Foo getFoo(){
        return foo; //Any thread is guaranteed to see the latest write to foo
    }
    
    void updateFooCount(int count){
        foo.setCount(count); // Foo's count variable is not guaranteed memory visibility, only this Foo reference itself is
    }
}
```
The `volatile` keyword does this by inserting a `store-store` memory barrier before the field write and a `store-load` barrier after the volatile write
- A `store-store` barrier prevents writes before a volatile write from being reordered after it and ensures those writes are flushed to main memory 
- A `store-load` barrier is inserted after a volatile write to prevent subsequent reads from being reordered before a write to that volatile field.

The `volatile` keyword also ensures that subsequents reads after a read from a volatile field see fresh values from main memory. It does this by inserting a `load-load` and a `load-store` barrier after the volatile read.
- A `load-load` barrier ensures that reads before the volatile read are not reordered with reads after it
- A `load-store` barrier ensures that subsequent reads and writes operations after a volatile read cannot be reordered by to happen before the volatile read

## Synchronized happens before guarantee
The synchronized keyword has two happens before guarantees. 
1. When a thread enters a synchronized block, all variables visible to the thread will be read in from main memory
To uphold these guarantees, a `load-load` and a `load-store` acquire barrier is at the entrance of the synchronized block.
- The `load-load` barrier prevents reads from inside the synchronized block from being reordered before the lock acquisition. It also ensures all values visible to a thread when it enters a synchronized block are read from main memory.
- The `load-store` barrier prevents writes from inside the synchronized block from being reordered before the lock acquisition


2. When a thread exits a synchronized block, all variable modified by the thread will be written to main memory
To uphold these guarantees, a `store-store` and a `store-load` release barrier are placed at the exit of a synchronized block
- The `store-store` barrier prevents writes from inside the synchronized block from being reordered after an unlock of a synchronized block. It also ensures writes that happen before and in the synchronized block are flushed to main memory once the thread exits the synchronized block.
- The `store-load` barrier prevents reads from inside the synchronized block from being reordered after an unlock of a synchronized block

## Other straightforward happens before guarantees
These guarantees are rather straightforward and don't need much of an explanation
- Monitor lock rule: An unlock on a monitor lock happens before every subsequent lock on that same monitor lock by any thread. This ensures at most one thread can hold a monitor lock at every point in time
-  Thread start rule: A call to `Thread#start` on a thread happens before every action in the started thread.
- Thread termination rule: Any action in a thread happens before any other thread detects that thread has terminated
- Interruption rule: A thread calling `Thread#interrupt` on another thread happens before the interrupted thread detects the interrupt
- Transitivity: If A happens before B, and B happens before C, then A happens before C.