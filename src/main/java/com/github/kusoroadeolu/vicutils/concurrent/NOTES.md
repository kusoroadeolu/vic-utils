# Some notes about Java's Memory Model
## Happens before guarantees
### Every action in a thread happens before every action that comes after that operation's order. 
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
Ideally in an all perfect world, thread 1 would ideally write to x and y before thread 2 reads. But this is where memory reordering comes into play. `Thread 1` will view it's writes in the order the occurred due to the **happens before** guarantee, 
</br> but `Thread 2` might see that only `y` has been written, read both values for `x` and `y` before seeing a write to `x`. To prevent this synchronization is needed

### Every volatile write to a shared variable happens before every volatile subsequent volatile read
The JMM specification states that before every volatile write to a variable occurs before a volatile read of that same variable. The `volatile` keyword acts as a weaker synchronization primitive, it ensures immediate visibility of writes to shared variables, by flushing writes to that variable immediately to main memory 
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
The `volatile` keyword also ensures that writes to shared variables that **happens before** a write to a volatile field are automatically made visible to other threads. It does this by inserting a `store-store` memory barrier before the field write. 
- A `store-store` barrier prevents the CPU from reordering operations after the volatile write and also flushes writes before the volatile write to main memory.
- A `store-load` memory barrier is also inserted after the volatile write to prevent subsequent writes from being reordered before a write to that volatile field.

The `volatile` keyword also ensures that subsequents reads after a read from a volatile field see fresh values from main memory. It does this by inserting a `load-load` and a `load-store` barrier after the volatile read.
- A `load-load` barrier ensures that subsequent reads after a volatile read see fresh values from main memory. This is achieved by forcing the thread to invalidate all stored data in its caches and registries and instead fetch from main memory.
- A `load-store` barrier ensures that subsequent reads and writes operations after a volatile read cannot be reordered by the CPU to occur before that volatile read


