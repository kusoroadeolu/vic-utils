import com.github.kusoroadeolu.vicutils.concurrent.actors.ActorRef;
import com.github.kusoroadeolu.vicutils.concurrent.actors.ActorSystem;
import com.github.kusoroadeolu.vicutils.concurrent.actors.Counter;
import com.github.kusoroadeolu.vicutils.concurrent.actors.Message;

void main() throws InterruptedException {
    ActorRef<Message> ref = ActorSystem.getContext().createActor(Counter::new, "Parent Counter");
    ActorRef<Message> child = ref.spawn(Counter::new, "Child Counter");
    child.tell(new Counter.ExMessage(new RuntimeException()));
    Thread.sleep(100);
    String address = child.toString();
    ActorRef<Message> restarted = ActorSystem.getContext().getActor(address);
    IO.println(restarted.getParent());

}