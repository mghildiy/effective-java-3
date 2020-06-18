package creatinganddestroyingobjects;

public class Test {

    public  void main(String[] args) {
        MyClass instance = new MyClass.Builder(101, 1).name("Test Name").status("Test Status").lat(12.56).lon(56.12).build();
    }
}
