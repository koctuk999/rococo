package guru.qa.rococo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class Helpers {

    public static String getPhotoByPath(String path) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of(path));
        return new String(Base64.getEncoder().encode(imageBytes));
    }
}
