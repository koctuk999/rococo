package guru.qa.rococo.tests.web;

import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.core.annotations.GeneratedMuseum;
import guru.qa.rococo.page.MuseumPage;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.CustomAssert.check;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Museum tests")
public class MuseumTest extends BaseWebTest {

    @Test
    @GeneratedMuseum
    @DisplayName("Check museum")
    public void getMuseum(Museum museum) {
        MuseumPage museumPage = mainPage
                .open()
                .toMuseumPage()
                .searchMuseum(museum.getTitle())
                .clickMuseum(museum.getTitle())
                .waitForPageLoaded();

        check("museum have expected title",
                museumPage.getTitle(), equalTo(museum.getTitle())
        );

        check("museum have expected description",
                museumPage.getDescription(), equalTo(museum.getDescription())
        );

        check("museum have expected country",
                museumPage.getCountry(), equalTo(
                        museum
                                .getGeo()
                                .getCountry()
                                .getName()
                )
        );

        check("museum have expected city",
                museumPage.getCity(), equalTo(
                        museum
                                .getGeo()
                                .getCity()
                )
        );

        check("museum have expected photo",
                museumPage.getPhoto(), equalTo(museum.getPhoto())
        );


    }
}
