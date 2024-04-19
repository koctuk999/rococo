package guru.qa.rococo.tests.web;

import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.GeneratedArtist;
import guru.qa.rococo.core.annotations.GeneratedMuseum;
import guru.qa.rococo.core.annotations.GeneratedPainting;
import guru.qa.rococo.page.PaintingPage;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.CustomAssert.check;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Painting tests")
public class PaintingTest extends BaseWebTest {

    @Test
    @DisplayName("Check painting")
    @GeneratedPainting(artist = @GeneratedArtist, museum = @GeneratedMuseum)
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
