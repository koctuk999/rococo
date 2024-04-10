package guru.qa.rococo.tests.web;

import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.core.annotations.TestMuseum;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Museum tests")
public class MuseumWebTest extends BaseWebTest {

    @Test
    @TestMuseum
    @DisplayName("Check museum")
    public void getMuseum(Museum museum) {
        mainPage
                .open()
                .toMuseumPage()
                .waitForPageLoaded();
    }
}
