import com.github.kusoroadeolu.vicutils.concurrent.mutex.Mutex;

Mutex mutex = new Mutex();
List<Integer> add = new ArrayList<>();
void main() throws InterruptedException {


}

void add(int i){

    mutex.acquire();
    try {
        add.add(i);
    }finally {
        mutex.release();
    }
}

