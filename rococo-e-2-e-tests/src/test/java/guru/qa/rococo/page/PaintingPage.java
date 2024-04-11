package guru.qa.rococo.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.className;

public class PaintingPage extends BasePage<PaintingPage> {

    private final SelenideElement title = $(className("qa-painting-title"));
    private final SelenideElement description = $(className("qa-painting-description"));
    private final SelenideElement artist = $(className("qa-painting-artist"));
    private final SelenideElement content = $(className("qa-painting-content"));

    public String getTitle() {
        return title.text();
    }

    public String getDescription() {
        return description.text();
    }

    public String getArtist() {
        return artist.text();
    }

    public String getContent() {
        return content.getAttribute("src");
    }

    @Override
    public PaintingPage waitForPageLoaded() {
        title.shouldBe(not(text("undefined")));
        return this;
    }
}
