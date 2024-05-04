package guru.qa.rococo.page.painting;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.selenide.condition.ImageCondition.imageCondition;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class PaintingPage extends BasePage<PaintingPage> {

    private final SelenideElement title = $(byDataTestId("painting-title"));
    private final SelenideElement description = $(byDataTestId("painting-description"));
    private final SelenideElement artist = $(byDataTestId("painting-artist"));
    private final SelenideElement content = $(byDataTestId("painting-content"));
    private final SelenideElement editButton = $(byDataTestId("edit-painting"));

    @Override
    @Step("Wait for the page is loaded")
    public PaintingPage waitForPageLoaded() {
        title.shouldBe(visible);
        title.shouldBe(not(text("undefined")));
        content.shouldBe(visible);
        content.shouldBe(not(text("undefined")));
        return this;
    }

    public boolean isEditAvailable() {
        return editButton.exists();
    }

    @Step("Check title {0}")
    public PaintingPage checkTitle(String title) {
        this.title.should(text(title));
        return this;
    }

    @Step("Check description {0}")
    public PaintingPage checkDescription(String description) {
        this.description.should(text(description));
        return this;
    }

    @Step("Check artist {0}")
    public PaintingPage checkArtist(String artist) {
        this.artist.should(text(artist));
        return this;
    }

    @Step("Check content {0}")
    public PaintingPage checkContent(String content, boolean byPath) {
        this
                .content
                .should(imageCondition(content, byPath));
        return this;
    }

    @Step("Edit painting")
    public PaintingUpsertModal editPainting() {
        this.editButton.click();
        return new PaintingUpsertModal();
    }

}
