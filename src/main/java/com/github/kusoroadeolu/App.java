import com.github.kusoroadeolu.vicutils.concurrent.channels.BufferedChannel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.Channel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.ChannelSelector;
import com.github.kusoroadeolu.vicutils.concurrent.channels.UnBufferedChannel;

void main() throws InterruptedException {
    Thread.startVirtualThread(() -> {
        threw(_ -> {
            throw new RuntimeException();
        });
    });

    Thread.sleep(10000);
}

void threw(Consumer<Integer> integerConsumer){
    integerConsumer.accept(1);
}