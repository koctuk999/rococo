package guru.qa.rococo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ImageHelper {

    public static final String MUSEUM_PHOTO_PATH = "images/museum_image_1.jpg";
    public static final String ARTIST_PHOTO_PATH = "images/artist_image_1.jpg";
    public static final String PAINTING_PHOTO_PATH = "images/painting_image_1.jpg";

    public static String getPhotoByPath(String imagePath) throws Exception {
        InputStream is = ImageHelper.class.getClassLoader().getResourceAsStream(imagePath);
        return "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(is.readAllBytes()), StandardCharsets.UTF_8);
    }
}
