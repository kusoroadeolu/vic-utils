import com.github.kusoroadeolu.vicutils.concurrent.locked.Locked;

void main(){
    Locked<User> locked = new Locked<>(new User()); //Locks all ops in this class with a reentrant lock
    locked.consume(u -> u.setName("Vic"));
    locked.supply(User::getName);
}

class User{
    int id;
    String name;

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}



