import com.github.kusoroadeolu.vicutils.concurrent.mutex.NaiveMutex;
import com.github.kusoroadeolu.vicutils.ds.Trie;
import com.github.kusoroadeolu.vicutils.misc.Try;

import java.util.concurrent.atomic.AtomicReference;

NaiveMutex mutex = new NaiveMutex();
AtomicReference<Thread> reference = new AtomicReference<>();
void main() throws InterruptedException {
for (int i = 0; i < 2; i++){
    Thread.startVirtualThread(() -> Try.run(this::doSomething));
}

Thread.sleep(10000);

}

//this should block hopefully lol
void doSomething() throws InterruptedException {
mutex.acquire();
IO.println("hELLLO");
Thread.sleep(1000);

mutex.release();
}




