package guru.qa.rococo.tests;

import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.core.annotations.TestMuseum;
import org.junit.jupiter.api.Test;

public class MuseumTest extends BaseWebTest {

    @Test
    @TestMuseum
    public void getMuseum(Museum museum) {
        mainPage
                .open()
                .toMuseumPage()
                .waitForPageLoaded();
    }
}
