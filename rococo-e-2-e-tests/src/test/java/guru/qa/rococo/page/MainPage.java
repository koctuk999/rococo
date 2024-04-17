package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {
    private final SelenideElement paintings = $("img[alt='Ссылка на картины']");
    private final SelenideElement artists = $("img[alt='Ссылка на художников']");
    private final SelenideElement museums = $("img[alt='Ссылка на музеи']");

    public Header getHeader() {
        return header;
    }

    @Override
    public MainPage waitForPageLoaded() {
        paintings.should(visible);
        artists.should(visible);
        museums.should(visible);
        return this;
    }

    @Step("Open main page")
    public MainPage open() {
        Selenide.open(CFG.frontUrl());
        return this;
    }

    @Step("Open museum page")
    public MuseumsPage toMuseumPage() {
        museums.click();
        return new MuseumsPage();
    }

    @Step("Open artist page")
    public ArtistsPage toArtistPage() {
        artists.click();
        return new ArtistsPage();
    }

    @Step("Open painting page")
    public PaintingsPage toPaintingPage() {
        paintings.click();
        return new PaintingsPage();
    }
}
