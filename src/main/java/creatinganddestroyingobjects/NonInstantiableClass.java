package creatinganddestroyingobjects;

public class NonInstantiableClass {

    private NonInstantiableClass() {
        throw new AssertionError("This class can't be instantiated");
    }
}
