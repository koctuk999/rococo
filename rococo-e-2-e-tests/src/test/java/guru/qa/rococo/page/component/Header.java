package guru.qa.rococo.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.ArtistsPage;
import guru.qa.rococo.page.LoginPage;
import guru.qa.rococo.page.MuseumsPage;
import guru.qa.rococo.page.PaintingsPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.className;

public class Header extends BaseComponent<Header> {
    public Header() {
        super($("header[id='shell-header']"));
    }

    private final SelenideElement title = this.self.$("a[href*='/']");
    private final SelenideElement artist = this.self.$("a[href*='/artist']");
    private final SelenideElement museum = this.self.$("a[href*='/museum']");
    private final SelenideElement painting = this.self.$("a[href*='/painting']");

    private final SelenideElement login = this.self.$(className("qa-login"));
    private final SelenideElement avatar = this.self.$(className("avatar-initials"));

    @Step("Return to Main Page")
    public void toMainPage() {
        title.click();
    }

    @Step("To Museum Page")
    public MuseumsPage toMuseumPage() {
        museum.click();
        return new MuseumsPage();
    }

    @Step("To Artist Page")
    public ArtistsPage toArtistPage() {
        artist.click();
        return new ArtistsPage();
    }

    @Step("To Painting Page")
    public PaintingsPage toPaintingPage() {
        painting.click();
        return new PaintingsPage();
    }

    @Step("To Login Page")
    public LoginPage toLoginPage() {
        login.click();
        return new LoginPage();
    }

    @Step("Check that user is logged in")
    public void checkLoggedIn() {
        avatar.shouldBe(visible);
    }
}
