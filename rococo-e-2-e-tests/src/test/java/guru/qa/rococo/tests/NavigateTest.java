package guru.qa.rococo.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Common tests")
public class NavigateTest extends BaseWebTest {

    @DisplayName("First simple test")
    @Test
    public void firstTest() {
        mainPage
                .open()
                .waitForPageLoaded()
                .toMuseumPage()
                .waitForPageLoaded()
                .getHeader()
                .toMainPage();
        mainPage
                .waitForPageLoaded()
                .toArtistPage()
                .waitForPageLoaded()
                .getHeader()
                .toMainPage();
        mainPage
                .waitForPageLoaded()
                .toPaintingPage()
                .waitForPageLoaded()
                .getHeader()
                .toMainPage();


    }
}
