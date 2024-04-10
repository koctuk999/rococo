package guru.qa.rococo.tests.web;

import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Navigation tests")
public class NavigateWebTest extends BaseWebTest {

    @DisplayName("Simple navigation test")
    @Test
    public void simpleTest() {
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
