package guru.qa.rococo.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {
    private final SelenideElement paintings = $("img[alt='Ссылка на картины']");
    private final SelenideElement artists = $("img[alt='Ссылка на художников']");
    private final SelenideElement museums = $("img[alt='Ссылка на музеи']");

    @Step("Open main page")
    public MainPage open() {
        Selenide.open(CFG.frontUrl());
        return this;
    }

    @Override
    @Step("Wait for main page loaded")
    public MainPage waitForPageLoaded() {
        paintings.should(visible);
        artists.should(visible);
        museums.should(visible);
        return this;
    }

    @Step("Open museum page")
    public MuseumPage toMuseumPage() {
        museums.click();
        return new MuseumPage();
    }

    @Step("Open artist page")
    public ArtistPage toArtistPage() {
        artists.click();
        return new ArtistPage();
    }

    @Step("Open painting page")
    public PaintingPage toPaintingPage() {
        paintings.click();
        return new PaintingPage();
    }
}
