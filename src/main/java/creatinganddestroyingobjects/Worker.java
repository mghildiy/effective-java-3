package creatinganddestroyingobjects;

public class Worker {

    final Resource res;

    public Worker(Resource res) {
        this.res = res;
    }

    public void doStuff() {
        res.getIt();
    }
}
