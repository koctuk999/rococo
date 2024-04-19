package guru.qa.rococo.tests.web;


import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.core.annotations.GeneratedArtist;
import guru.qa.rococo.page.ArtistPage;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.CustomAssert.check;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Artists tests")
public class ArtistTest extends BaseWebTest {

    @Test
    @GeneratedArtist
    @DisplayName("Check artist")
    public void getArtist(Artist artist) {
        ArtistPage artistPage = mainPage
                .open()
                .toArtistPage()
                .clickArtist(artist.getName())
                .waitForPageLoaded();

        check(
                "artist have expected name",
                artistPage.getName(), equalTo(artist.getName())
        );

        check(
                "artist have expected biography",
                artistPage.getBiography(), equalTo(artist.getBiography())
        );

        check(
                "artist have expected photo",
                artistPage.getPhoto(), equalTo(artist.getPhoto())
        );
    }
}
