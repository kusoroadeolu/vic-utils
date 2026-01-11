import com.github.kusoroadeolu.vicutils.concurrent.mutex.Mutex;

Mutex mutex = new Mutex();
List<Integer> add = new ArrayList<>();
void main() {
    String s = "1235";
    IO.println(val);

}

void add(int i){

    mutex.acquire();
    try {
        add.add(i);
    }finally {
        mutex.release();
    }
}

