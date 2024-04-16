package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;

public class ArtistPage extends BasePage<ArtistPage> {

    private final SelenideElement name = $(className("qa-artist-name"));
    private final SelenideElement biography = $(className("qa-artist-biography"));
    private final SelenideElement photo = $(className("qa-artist-photo")).$(className("avatar-image"));

    @Override
    public ArtistPage waitForPageLoaded() {
        name.shouldBe(not(text("undefined")));
        return this;
    }


    public String getName() {
        return name.text();
    }

    public String getBiography() {
        return biography.text();
    }

    public String getPhoto() {
        return photo.getAttribute("src");
    }

}
