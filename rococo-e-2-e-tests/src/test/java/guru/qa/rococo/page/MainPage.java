package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.artist.ArtistsPage;
import guru.qa.rococo.page.component.Header;
import guru.qa.rococo.page.museum.MuseumsPage;
import guru.qa.rococo.page.painting.PaintingsPage;
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
        header.getSelf().should(visible);
        return this;
    }

    @Step("Open main page")
    public MainPage open() {
        Selenide.open(CFG.frontUrl());
        return this.waitForPageLoaded();
    }

    @Step("Open museum page")
    public MuseumsPage toMuseumsPage() {
        museums.click();
        return new MuseumsPage().waitForPageLoaded();
    }

    @Step("Open artist page")
    public ArtistsPage toArtistsPage() {
        artists.click();
        return new ArtistsPage().waitForPageLoaded();
    }

    @Step("Open painting page")
    public PaintingsPage toPaintingsPage() {
        paintings.click();
        return new PaintingsPage().waitForPageLoaded();
    }
}
