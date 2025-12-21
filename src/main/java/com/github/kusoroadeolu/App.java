import com.github.kusoroadeolu.vicutils.concurrent.semaphore.Chamaphore;

int variable = 0;

void main() throws InterruptedException {
    Chamaphore chamaphore = new Chamaphore(2);

    Thread.startVirtualThread(() -> {
        chamaphore.acquire();
        IO.println("Acquired permit");
        variable++;
        try {
            IO.println("Variable: " + variable);
            Thread.sleep(5000);
            chamaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Simulate work;
    });

    Thread.startVirtualThread(() -> {
        chamaphore.acquire();
        IO.println("Acquired permit");
        variable++;
        try {
            IO.println("Variable: " + variable);
            Thread.sleep(10000);
            chamaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Simulate work;
    });

    Thread.startVirtualThread(() -> {
        chamaphore.acquire();
        IO.println("Acquired permit");
        variable++;
        IO.println("Variable: " + variable);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Simulate work;
    });

    Thread.sleep(100000);
}
