import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Implementira shranjevanje in nalaganje igre v JSON formatu.
 */
public class Shranjevanje {
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Shrani stanje igre na izbrano pot.
     *
     * @param model
     * @param pot
     * @throws IOException
     */
    void shrani (ModelIgre model, Path pot) throws IOException {
        try (OutputStream os = Files.newOutputStream(pot, StandardOpenOption.CREATE)) {
            mapper.writeValue(os, model.verzijaZaShranjevanje());
        }
    }

    /**
     * Prebere stanje igre in ga poda trenutnemu modelu.
     *
     * @param model
     * @param pot
     * @throws IOException
     */
    void preberi (ModelIgre model, Path pot) throws IOException {
        try (InputStream is = Files.newInputStream(pot)) {
            ShranjenaIgra igra = mapper.readValue(is, ShranjenaIgra.class);
            model.preberi(igra);
        }
    }
}
