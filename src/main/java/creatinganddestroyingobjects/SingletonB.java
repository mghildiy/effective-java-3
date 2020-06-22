package creatinganddestroyingobjects;

public class SingletonB {
    private static SingletonB INSTANCE = new SingletonB();

    private SingletonB() {

    }

    public static SingletonB getInstance() {
        return INSTANCE;
    }

    public void doYourStuff() {

    }
}
