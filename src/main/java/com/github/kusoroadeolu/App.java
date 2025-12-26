import com.github.kusoroadeolu.vicutils.concurrent.actors.ActorRef;
import com.github.kusoroadeolu.vicutils.concurrent.actors.ActorSystem;
import com.github.kusoroadeolu.vicutils.concurrent.actors.Counter;
import com.github.kusoroadeolu.vicutils.concurrent.actors.Message;


void main() throws InterruptedException {
    ActorRef<Message> m = ActorSystem.createActor(Counter::new);
    ActorRef<Message> cm = m.spawn(Counter::new);
    IO.println(cm);
    //Thread.sleep(1000);
}



