import com.github.kusoroadeolu.vicutils.concurrent.semaphore.Barrier;

int variable = 0;

void main() throws InterruptedException {
    Barrier b = new Barrier(3);
    Thread.startVirtualThread(() -> {
        try {
            b.await();
            IO.println("Done");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });

    Thread.startVirtualThread(() -> {
        try {
            b.await();
            IO.println("Done");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });

    Thread.startVirtualThread(() -> {
        try {
            Thread.sleep(3000);
            b.await();
            IO.println("Done");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });


    Thread.sleep(100000);

}
