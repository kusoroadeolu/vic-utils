import com.github.kusoroadeolu.vicutils.concurrent.mutex.Mutex;

private final static ArrayBlockingQueue<Task> taskPool = new ArrayBlockingQueue<>(2);

void main() {
    Task t = new Task("123");
    Task t1 = new Task("123");
    IO.println(t.equals(t1));
}

record Task(String s){}




