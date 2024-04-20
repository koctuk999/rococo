package guru.qa.rococo.page.artist;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.rococo.selenide.condition.PhotoCondition.imageCondition;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class ArtistPage extends BasePage<ArtistPage> {

    private final SelenideElement name = $(byDataTestId("artist-name"));
    private final SelenideElement biography = $(byDataTestId("artist-biography"));
    private final SelenideElement photo = $(byDataTestId("artist-photo"));

    private final ElementsCollection paintings = $$(byDataTestId("painting-item"));

    private final SelenideElement addPaintingButton = $(byText("Добавить картину"));
    private final SelenideElement editButton = $(byDataTestId("edit-artist"));

    @Override
    @Step("Wait for the page is loaded")
    public ArtistPage waitForPageLoaded() {
        name.should(visible);
        name.should(not(text("undefined")));
        photo.should(visible);
        photo.should(not(text("undefined")));
        return this;
    }


    @Step("Check name {0}")
    public ArtistPage checkName(String name) {
        this.name.should(text(name));
        return this;
    }

    @Step("Check biography {0}")
    public ArtistPage checkBiography(String biography) {
        this.biography.should(text(biography));
        return this;
    }

    @Step("Check photo {0}")
    public ArtistPage checkPhoto(String photo, Boolean byPath) {
        this.photo.should(imageCondition(photo, byPath));
        return this;
    }

}
