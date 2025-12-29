public static Map<String, UnsafeClass> MAP = new HashMap<>();
public static Map<String, SafeClass> SAFE_MAP = new ConcurrentHashMap<>();


void main() throws InterruptedException {
    Thread.startVirtualThread(() -> {
        while (true){
            IO.println(MAP.get("class1"));
        }
    });

    UnsafeClass unsafeClass = new UnsafeClass(1, "class1");


}

class UnsafeClass{
    private final int count;
    private final String name;

    public UnsafeClass(int count, String name) throws InterruptedException {
        this.name = name;
        MAP.put(name, this); //This escapes here
        Thread.sleep(5000);
        this.count = count;
    } //Unsafely published class even though its fields are final. The this reference will escape

    @Override
    public String toString() {
        return "UnsafeClass{" +
                "count = " + count +
                ", name = " + name +
                '}';
    }
}

static class SafeClass{
    private final int count;
    private final String name;

    private SafeClass(int count, String name)  {
        this.name = name;
        this.count = count;
    }

    public static SafeClass create(int count, String name){
        SafeClass sc = new SafeClass(count, name);
        SAFE_MAP.put(sc.name, sc);
        return sc;
    }
}
