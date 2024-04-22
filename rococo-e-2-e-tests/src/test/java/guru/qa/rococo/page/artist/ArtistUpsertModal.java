package guru.qa.rococo.page.artist;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class ArtistUpsertModal extends BasePage<ArtistUpsertModal> {

    private final SelenideElement pageTitle = $(byText("Редактировать художника"));
    private final SelenideElement root = $(byDataTestId("modal-component"));
    private final SelenideElement photo = root.$(byDataTestId("artist-photo"));

    private final SelenideElement photoInput = root.$("input[name='photo']");
    private final SelenideElement nameInput = root.$("input[name='name']");
    private final SelenideElement biographyInput = root.$("textarea[name='biography']");

    private final SelenideElement saveButton = root.$("button[type='submit']");
    private final SelenideElement closeButton = root.$(byText("Закрыть"));

    @Override
    public ArtistUpsertModal waitForPageLoaded() {
        root.should(visible);
        pageTitle.should(visible);
        return this;
    }

    @Step("Set photo {0}")
    public ArtistUpsertModal setPhoto(String photo) {
        this.photoInput.uploadFromClasspath(photo);
        return this;
    }

    @Step("Set name {0}")
    public ArtistUpsertModal setName(String name) {
        this.nameInput.setValue(name);
        return this;
    }

    @Step("Set biography {0}")
    public ArtistUpsertModal setBiography(String biography) {
        this.biographyInput.setValue(biography);
        return this;
    }

    @Step("Click save button")
    public ArtistsPage submit() {
        saveButton.click();
        return new ArtistsPage();
    }
}
