import com.github.kusoroadeolu.vicutils.concurrent.*;

void main(){
    Channel<Integer> chan = new BufferedChannel<>(2);
    Channel<Integer> chan2 = new BufferedChannel<>(2);
    Channel<Integer> chan3 = new BufferedChannel<>(2);
    chan.make();
    chan2.make();
    chan3.make();
    chan.send(1);
    chan2.send(2);
    chan3.send(3);

    Integer val = ChannelSelector.select(chan, chan2, chan3).timeout(2000).execute();
    IO.println(val);
}