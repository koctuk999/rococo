package guru.qa.rococo.tests.web;

import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.CLIENT_ACCEPTANCE;

@DisplayName("Navigation web tests")
@Tag(CLIENT_ACCEPTANCE)
public class NavigateWebTest extends BaseWebTest {

    @DisplayName("Simple navigation test")
    @Test
    public void simpleNavigationTest() {
        mainPage
                .open()
                .waitForPageLoaded()
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
}
