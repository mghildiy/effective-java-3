package creatinganddestroyingobjects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TryFinally {

    public String readFrom(String path) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(path));) {
            return br.readLine();
        }
    }
}
