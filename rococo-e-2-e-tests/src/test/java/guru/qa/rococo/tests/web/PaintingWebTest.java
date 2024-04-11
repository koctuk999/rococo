package guru.qa.rococo.tests.web;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.TestArtist;
import guru.qa.rococo.core.annotations.TestMuseum;
import guru.qa.rococo.core.annotations.TestPainting;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Painting test")
public class PaintingWebTest extends BaseWebTest {

    @Test
    @DisplayName("Check painting")
    @TestPainting(artist = @TestArtist, museum = @TestMuseum)
    public void getPainting(Painting painting, Artist artist, Museum museum) {
        mainPage
                .open()
                .toPaintingPage()
                .waitForPageLoaded();
    }
}
