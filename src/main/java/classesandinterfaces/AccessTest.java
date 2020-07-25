package classesandinterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessTest {

    private final List<String> names = new ArrayList<>();
    public List<String> UNMOFIDIABLENAMES = Collections.unmodifiableList(names);

    public AccessTest(String safeName){
        this.names.add(safeName);
    }

    public List<String> getNames() {
        List<String> clone = new ArrayList<>(this.names);
        return clone;
    }

    public static void main(String[] args) {
        AccessTest at = new AccessTest("Safe name");
        List clone1 = at.getNames();
        clone1.add("Dangerous name");
        assert at.UNMOFIDIABLENAMES.size() == 1;
        assert at.UNMOFIDIABLENAMES.contains("Safe name");
        List clone2 = at.getNames();
        clone2.set(0, "Dangerous name");
        assert at.UNMOFIDIABLENAMES.size() == 1;
        assert at.UNMOFIDIABLENAMES.contains("Safe name");
    }
}
