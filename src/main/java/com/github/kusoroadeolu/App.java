import com.github.kusoroadeolu.vicutils.concurrent.actors.Personalities;
import com.github.kusoroadeolu.vicutils.concurrent.actors.Personality;
import com.github.kusoroadeolu.vicutils.concurrent.actors.Smth;

int variable = 0;

void main() throws InterruptedException {
    Personality<String> personality = Smth.setup();
    boolean b = Personalities.<String>same() == personality;
    IO.println(personality);


}



