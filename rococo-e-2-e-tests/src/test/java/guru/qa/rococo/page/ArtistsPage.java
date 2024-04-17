package guru.qa.rococo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.className;

public class ArtistsPage extends BasePage<ArtistsPage> {
    private final SelenideElement searchInput = $("input[title='Искать художников...']");

    private final ElementsCollection artists = $$(className("qa-artist-item"));

    public Header getHeader() {
        return header;
    }

    @Override
    public ArtistsPage waitForPageLoaded() {
        searchInput.should(visible);
        return this;
    }

    @Step("Click artist")
    public ArtistPage clickArtist(String artistName) {
        artists
                .findBy(text(artistName))
                .$(byClassName("avatar-image"))
                .click();
        return new ArtistPage();
    }
}
