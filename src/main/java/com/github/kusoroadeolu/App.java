import com.github.kusoroadeolu.vicutils.concurrent.mutex.NaiveMutex;

NaiveMutex mutex = new NaiveMutex();
List<Integer> add = new ArrayList<>();
void main() throws InterruptedException {
for (int i = 0; i < 100; i++){
    final int j = i;
    Thread.startVirtualThread(() -> this.add(j));
}

Thread.sleep(1000);
IO.println(add.size());

}

void add(int i){
    mutex.acquire();
    try {
        add.add(i);
    }finally {
        mutex.release();
    }
}

