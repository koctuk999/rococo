package guru.qa.rococo.page.museum;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.selenide.condition.ImageCondition.imageCondition;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class MuseumPage extends BasePage<MuseumPage> {

    private final SelenideElement title = $(byDataTestId("museum-title"));
    private final SelenideElement description = $(byDataTestId("museum-description"));
    private final SelenideElement geo = $(byDataTestId("museum-geo"));
    private final SelenideElement photo = $(byDataTestId("museum-photo"));

    private final SelenideElement editButton = $(byDataTestId("edit-museum"));

    @Override
    @Step("Wait for the page is loaded")
    public MuseumPage waitForPageLoaded() {
        title.should(visible);
        title.should(not(text("undefined")));
        photo.should(visible);
        photo.should(not(text("undefined")));
        return this;
    }

    public boolean isEditAvailable(){
        return editButton.exists();
    }

    @Step("Edit museum")
    public MuseumUpsertModal editMuseum(){
        editButton.click();
        return new MuseumUpsertModal();
    }
    @Step("Check title {0}")
    public MuseumPage checkTitle(String title) {
        this.title.should(text(title));
        return this;
    }

    @Step("Check description {0}")
    public MuseumPage checkDescription(String description) {
        this.description.should(text(description));
        return this;
    }

    @Step("Check country {0}")
    public MuseumPage checkCountry(String country) {
        this.geo.should(partialText(country));
        return this;
    }

    @Step("Check city {0}")
    public MuseumPage checkCity(String city) {
        this.geo.should(partialText(city));
        return this;
    }

    @Step("Check photo {0}")
    public MuseumPage checkPhoto(String photo, Boolean byPath) {
        this.photo.should(imageCondition(photo, byPath));
        return this;
    }

}
