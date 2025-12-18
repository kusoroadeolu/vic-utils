import com.github.kusoroadeolu.vicutils.concurrent.*;

void main(){
    Channel<Integer> chan = new BufferedChannel<>(2);
    Channel<Integer> chan2 = new BufferedChannel<>(2);
    Channel<Integer> chan3 = new BufferedChannel<>(2);
    chan.make();
    chan2.make();
    chan3.make();

}