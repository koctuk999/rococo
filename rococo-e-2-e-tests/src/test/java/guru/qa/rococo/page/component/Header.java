package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.ArtistsPage;
import guru.qa.rococo.page.MuseumsPage;
import guru.qa.rococo.page.PaintingsPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {
    public Header() {
        super($("header[id='shell-header']"));
    }

    private final SelenideElement title = this.self.$("a[href*='/']");
    private final SelenideElement artist = this.self.$("a[href*='/artist']");
    private final SelenideElement museum = this.self.$("a[href*='/museum']");
    private final SelenideElement painting = this.self.$("a[href*='/painting']");

    @Step("Return to MainPage")
    public void toMainPage() {
        title.click();
    }

    @Step("To MuseumPage")
    public MuseumsPage toMuseumPage() {
        museum.click();
        return new MuseumsPage();
    }

    @Step("To ArtistPage")
    public ArtistsPage toArtistPage() {
        artist.click();
        return new ArtistsPage();
    }

    @Step("To PaintingPage")
    public PaintingsPage toPaintingPage() {
        painting.click();
        return new PaintingsPage();
    }
}
