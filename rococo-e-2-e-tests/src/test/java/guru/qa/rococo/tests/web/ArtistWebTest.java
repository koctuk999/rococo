package guru.qa.rococo.tests.web;


import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.core.annotations.TestArtist;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Artists test")
public class ArtistWebTest extends BaseWebTest {

    @Test
    @TestArtist
    @DisplayName("Check artist")
    public void getArtist(Artist artist){
          mainPage
                  .open()
                  .toArtistPage()
                  .waitForPageLoaded();
    }
}
