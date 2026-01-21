import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

Object lock1 = new Object();
Object lock2 = new Object();

void main() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(2);
    ThreadMXBean bean = ManagementFactory.getThreadMXBean();

     Thread.ofPlatform()
    .name("V1")
    .start(() -> {
        synchronized (lock1) {
            try {
                Thread.sleep(100);
                synchronized (lock2) {
                    IO.println("Managed to acquire lock 2");
                    latch.countDown();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });

    Thread.ofPlatform().name("V2").start(() -> {
        synchronized (lock2){
            try {
                Thread.sleep(100);
                synchronized (lock1){
                    IO.println("Managed to acquire lock 1");
                    latch.countDown();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });


    Thread.sleep(3000);
    long[] ids = bean.findMonitorDeadlockedThreads();
    long count = bean.getThreadCount();

        IO.println(count);
        IO.println(Arrays.toString(ids));
        latch.await();


}





