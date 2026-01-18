import com.github.kusoroadeolu.vicutils.concurrent.mutex.Mutex;

private final static ArrayBlockingQueue<Task> taskPool = new ArrayBlockingQueue<>(2);

void main() {

}

static class Task{
    Runnable runnable;

    void setTask(Runnable runnable){
        this.runnable = runnable;
    }

    void run(){
        if (runnable != null) runnable.run();
    }

}




