package guru.qa.rococo.tests.web;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.TestArtist;
import guru.qa.rococo.core.annotations.TestMuseum;
import guru.qa.rococo.core.annotations.TestPainting;
import guru.qa.rococo.page.PaintingPage;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.CustomAssert.check;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Painting test")
public class PaintingWebTest extends BaseWebTest {

    @Test
    @DisplayName("Check painting")
    @TestPainting(artist = @TestArtist, museum = @TestMuseum)
    public void getPainting(Painting painting) {
        PaintingPage paintingPage = mainPage
                .open()
                .toPaintingPage()
                .clickPainting(painting.getTitle())
                .waitForPageLoaded();

        check(
                "painting have expected title",
                paintingPage.getTitle(), equalTo(painting.getTitle())
        );

        check(
                "painting have expected description",
                paintingPage.getDescription(), equalTo(painting.getDescription())
        );

        check(
                "painting have expected artist",
                paintingPage.getArtist(), equalTo(painting.getArtist().getName())
        );

        check(
                "painting have expected content",
                paintingPage.getContent(), equalTo(painting.getContent())
        );

    }
}
