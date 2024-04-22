package guru.qa.rococo.page.painting;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class PaintingUpsertModal extends BasePage<PaintingUpsertModal> {

    private final SelenideElement pageTitle = $(byText("Редактировать картину"));
    private final SelenideElement root = $(byDataTestId("modal-component"));
    private final SelenideElement content = root.$(byTagName("img"));
    private final SelenideElement contentInput = root.$("input[name='content']");
    private final SelenideElement titleInput = root.$("input[name='title']");
    private final SelenideElement artistSelect = root.$("select[name='authorId']");
    private final SelenideElement descriptionInput = root.$("textarea[name='description']");
    private final SelenideElement museumSelect = root.$("select[name='museumId']");
    private final SelenideElement saveButton = root.$("button[type='submit']");
    private final SelenideElement closeButton = root.$(byText("Закрыть"));

    @Override
    public PaintingUpsertModal waitForPageLoaded() {
        root.should(visible);
        pageTitle.should(visible);
        return this;
    }

    @Step("Set title {0}")
    public PaintingUpsertModal setTitle(String title) {
        this.titleInput.setValue(title);
        return this;
    }

    @Step("Set content {0}")
    public PaintingUpsertModal setContent(String content) {
        this.contentInput.uploadFromClasspath(content);
        return this;
    }

    @Step("Set description {0}")
    public PaintingUpsertModal setDescription(String description) {
        this.descriptionInput.setValue(description);
        return this;
    }

    @Step("Select artist {0}")
    public PaintingUpsertModal selectArtist(String artistName) {
        this.artistSelect.selectOptionContainingText(artistName);
        return this;
    }

    @Step("Select painting {0}")
    public PaintingUpsertModal selectMuseum(String museumTitle) {
        this.museumSelect.selectOptionContainingText(museumTitle);
        return this;
    }

    @Step("Click save button")
    public PaintingsPage submit() {
        saveButton.click();
        return new PaintingsPage();
    }
}
