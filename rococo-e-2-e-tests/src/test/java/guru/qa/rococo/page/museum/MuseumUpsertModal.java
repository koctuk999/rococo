package guru.qa.rococo.page.museum;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class MuseumUpsertModal extends BasePage<MuseumUpsertModal> {

    private final SelenideElement pageTitle = $(byText("Редактировать музей"));
    private final SelenideElement root = $(byDataTestId("modal-component"));

    private final SelenideElement photo = root.$(byTagName("img"));
    private final SelenideElement photoInput = root.$("input[name='photo']");
    private final SelenideElement titleInput = root.$("input[name='title']");
    private final SelenideElement countrySelect = root.$("select[name='countryId']");
    private final SelenideElement cityInput = root.$("input[name='city']");
    private final SelenideElement descriptionInput = root.$("textarea[name='description']");
    private final SelenideElement saveButton = root.$("button[type='submit']");
    private final SelenideElement closeButton = root.$(byText("Закрыть"));

    @Override
    public MuseumUpsertModal waitForPageLoaded() {
        root.should(visible);
        pageTitle.should(visible);
        return this;
    }

    @Step("Set photo {0}")
    public MuseumUpsertModal setPhoto(String photo) {
        this.photoInput.uploadFromClasspath(photo);
        return this;
    }

    @Step("Set title {0}")
    public MuseumUpsertModal setTitle(String title) {
        this.titleInput.setValue(title);
        return this;
    }

    @Step("Select country {0}")
    public MuseumUpsertModal selectCountry(String country) {
        this.countrySelect.selectOptionContainingText(country);
        return this;
    }

    @Step("Set city {0}")
    public MuseumUpsertModal setCity(String city) {
        this.cityInput.setValue(city);
        return this;
    }

    @Step("Set description {0}")
    public MuseumUpsertModal setDescription(String description) {
        this.descriptionInput.setValue(description);
        return this;
    }

    @Step("Click save button")
    public MuseumsPage submit() {
        saveButton.click();
        return new MuseumsPage();
    }
}
