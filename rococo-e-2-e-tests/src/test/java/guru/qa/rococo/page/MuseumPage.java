package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.className;

public class MuseumPage extends BasePage<MuseumPage> {

    private final SelenideElement title = $(className("qa-museum-title"));
    private final SelenideElement description = $(className("qa-museum-description"));
    private final SelenideElement geo = $(className("qa-museum-geo"));
    private final SelenideElement photo = $(className("qa-museum-photo"));

    @Override
    public MuseumPage waitForPageLoaded() {
        title.shouldBe(not(text("undefined")));
        return this;
    }

    public String getTitle() {
        return title.text();
    }

    public String getDescription() {
        return description.text();
    }

    public String getCountry() {
        return geo.text().split(", ")[0];
    }

    public String getCity() {
        return geo.text().split(", ")[1];
    }

    public String getPhoto() {
        return photo.getAttribute("src");
    }

}
