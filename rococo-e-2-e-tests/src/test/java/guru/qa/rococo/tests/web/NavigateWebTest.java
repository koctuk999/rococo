package guru.qa.rococo.tests.web;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.*;
import guru.qa.rococo.page.artist.ArtistPage;
import guru.qa.rococo.page.museum.MuseumPage;
import guru.qa.rococo.page.museum.MuseumsPage;
import guru.qa.rococo.page.painting.PaintingsPage;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.CLIENT_ACCEPTANCE;
import static guru.qa.rococo.utils.CustomAssert.check;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Navigation web tests")
@Tag(CLIENT_ACCEPTANCE)
public class NavigateWebTest extends BaseWebTest {

    @DisplayName("Simple navigation test")
    @Test
    public void simpleNavigationTest() {
        mainPage
                .open()
                .toMuseumsPage()
                .waitForPageLoaded()
                .getHeader()
                .toMainPage();

        mainPage
                .waitForPageLoaded()
                .toArtistsPage()
                .waitForPageLoaded()
                .getHeader()
                .toMainPage();

        mainPage
                .waitForPageLoaded()
                .toPaintingsPage()
                .waitForPageLoaded()
                .getHeader()
                .toMainPage();
    }

    @Test
    @LoggedIn(user = @CreatedUser)
    @GeneratedPainting(museum = @GeneratedMuseum, artist = @GeneratedArtist)
    @DisplayName("Check permissions authorized user")
    public void checkAuthorized(Artist artist, Museum museum, Painting painting) {
        boolean isAddArtistAvailable = mainPage
                .open()
                .toArtistsPage()
                .isAddArtistAvailable();
        boolean isEditArtistAvailable = mainPage
                .open()
                .toArtistsPage()
                .clickArtist(artist.getName())
                .isEditAvailable();

        boolean isAddMuseumAvailable = new ArtistPage()
                .getHeader()
                .toMuseumsPage()
                .isAddMuseumAvailable();
        boolean isEditMuseumAvailable = new MuseumsPage()
                .clickMuseum(museum.getTitle())
                .isEditAvailable();

        boolean isAddPaintingAvailable = new MuseumPage()
                .getHeader()
                .toPaintingsPage()
                .isAddPaintingAvailable();

        boolean isEditPaintingAvailable = new PaintingsPage()
                .clickPainting(painting.getTitle())
                .isEditAvailable();

        assertAll("Check permissions",
                () -> check("add artist is available",
                        isAddArtistAvailable, equalTo(true)),
                () -> check("edit artist is available",
                        isEditArtistAvailable, equalTo(true)),
                () -> check("add museum is available",
                        isAddMuseumAvailable, equalTo(true)),
                () -> check("edit museum is available",
                        isEditMuseumAvailable, equalTo(true)),
                () -> check("add painting is available",
                        isAddPaintingAvailable, equalTo(true)),
                () -> check("edit painting is available",
                        isEditPaintingAvailable, equalTo(true))
        );
    }

    @Test
    @GeneratedPainting(museum = @GeneratedMuseum, artist = @GeneratedArtist)
    @DisplayName("Check permissions unauthorized user")
    public void checkUnauthorized(Artist artist, Museum museum, Painting painting) {
        boolean isAddArtistAvailable = mainPage
                .open()
                .toArtistsPage()
                .isAddArtistAvailable();
        boolean isEditArtistAvailable = mainPage
                .open()
                .toArtistsPage()
                .clickArtist(artist.getName())
                .isEditAvailable();

        boolean isAddMuseumAvailable = new ArtistPage()
                .getHeader()
                .toMuseumsPage()
                .isAddMuseumAvailable();
        boolean isEditMuseumAvailable = new MuseumsPage()
                .clickMuseum(museum.getTitle())
                .isEditAvailable();

        boolean isAddPaintingAvailable = new MuseumPage()
                .getHeader()
                .toPaintingsPage()
                .isAddPaintingAvailable();

        boolean isEditPaintingAvailable = new PaintingsPage()
                .clickPainting(painting.getTitle())
                .isEditAvailable();

        assertAll("Check permissions",
                () -> check("add artist is not  available",
                        isAddArtistAvailable, equalTo(false)),
                () -> check("edit artist is not available",
                        isEditArtistAvailable, equalTo(false)),
                () -> check("add museum is not  available",
                        isAddMuseumAvailable, equalTo(false)),
                () -> check("edit museum is not available",
                        isEditMuseumAvailable, equalTo(false)),
                () -> check("add painting is not available",
                        isAddPaintingAvailable, equalTo(false)),
                () -> check("edit painting is not available",
                        isEditPaintingAvailable, equalTo(false))
        );
    }
}
