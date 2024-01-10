import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Shranjevanje {
    public static final String HEADER = "MoreOrLess.v1";
    void shrani (ModelIgre model, Path pot) throws IOException {
        try (OutputStream os = Files.newOutputStream(pot, StandardOpenOption.CREATE);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {

            oos.writeUTF(HEADER);
            oos.writeObject(model);
        }
    }

    ModelIgre preberi (Path pot) throws IOException {
        try (InputStream is = Files.newInputStream(pot);
             ObjectInputStream ois = new ObjectInputStream(is)){

            String header = ois.readUTF();
            if (!header.equals(HEADER)) {
                throw new IOException("Unsupported file format.");
            }
            try {
                return (ModelIgre) ois.readObject();
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException("Failed to read file.", e);
            }
        }
    }
}
