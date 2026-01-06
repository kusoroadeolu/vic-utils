import com.github.kusoroadeolu.vicutils.concurrent.mutex.Mutex;
import com.github.kusoroadeolu.vicutils.ds.ShitSkipList;

Mutex mutex = new Mutex();
List<Integer> add = new ArrayList<>();
void main() throws InterruptedException {
    ShitSkipList<Integer> list = new ShitSkipList<>(0.1);
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);
    List<Integer> ls = new ArrayList<>();
    IO.println(list.last());

    IO.println("Contains: " + list.contains(6));
}

void add(int i){

    mutex.acquire();
    try {
        add.add(i);
    }finally {
        mutex.release();
    }
}

