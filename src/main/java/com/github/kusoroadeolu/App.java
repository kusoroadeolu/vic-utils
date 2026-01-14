import com.github.kusoroadeolu.vicutils.concurrent.mutex.Mutex;

Mutex mutex = new Mutex();
List<Integer> add = new ArrayList<>();
void main() {
    IO.println(1 << 2);

}


class Node{
     int value;
     final ReentrantLock lock = new ReentrantLock();
     Node next;
}

void add(int i){

    mutex.acquire();
    try {
        add.add(i);
    }finally {
        mutex.release();
    }
}

