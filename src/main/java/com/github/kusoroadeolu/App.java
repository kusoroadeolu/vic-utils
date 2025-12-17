import com.github.kusoroadeolu.vicutils.concurrent.UnBufferedChannel;
import com.github.kusoroadeolu.vicutils.concurrent.ReceiveChannel;
import com.github.kusoroadeolu.vicutils.concurrent.SendChannel;

void main(){
    UnBufferedChannel<Integer> channel = new UnBufferedChannel<>();
    ReceiveChannel<Integer> rc = channel.makeReceiveChannel();
    SendChannel<Integer> sc = channel.makeSendChannel();
    sc.close();
    IO.println("123");
}