import com.github.kusoroadeolu.vicutils.concurrent.actors.ActorRef;
import com.github.kusoroadeolu.vicutils.concurrent.actors.ActorSystem;
import com.github.kusoroadeolu.vicutils.concurrent.actors.Counter;
import com.github.kusoroadeolu.vicutils.concurrent.actors.Message;

void main() throws InterruptedException {
    ActorRef<Message> ref = ActorSystem.createActor(Counter::new);
    ref.tell(new Counter.Increment(2));
    ref.tell(new Counter.Increment(3));
    Thread.sleep(1000);
}