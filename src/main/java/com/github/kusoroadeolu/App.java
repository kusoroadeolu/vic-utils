import com.github.kusoroadeolu.vicutils.concurrent.channels.BufferedChannel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.Channel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.ChannelSelector;

void main(){
    Channel<Runnable> chan = new BufferedChannel<>(2);
    Channel<Runnable> chan2 = new BufferedChannel<>(2);
    Channel<Runnable> chan3 = new BufferedChannel<>(2);
    chan.make();
    chan2.make();
    chan3.make();

    Runnable val = ChannelSelector.select(chan, chan2, chan3).timeout(2000).execute();
    IO.println(val);
}