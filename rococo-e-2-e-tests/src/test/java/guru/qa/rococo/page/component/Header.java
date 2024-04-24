package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.*;
import guru.qa.rococo.page.artist.ArtistsPage;
import guru.qa.rococo.page.museum.MuseumsPage;
import guru.qa.rococo.page.painting.PaintingsPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.selenide.condition.PhotoCondition.imageCondition;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class Header extends BaseComponent<Header> {
    public Header() {
        super($(byDataTestId("app-bar")));
    }

    private final SelenideElement title = this.self.$("a[href*='/']");
    private final SelenideElement artist = this.self.$("a[href*='/artist']");
    private final SelenideElement museum = this.self.$("a[href*='/museum']");
    private final SelenideElement painting = this.self.$("a[href*='/painting']");

    private final SelenideElement loginButton = $(byDataTestId("login-button"));
    private final SelenideElement profileButton = $(byDataTestId("profile-button"));

    private final SelenideElement avatar = profileButton.$(byDataTestId("avatar"));

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

    @Step("Click login button")
    public void clickLoginButton() {
        loginButton.click();
    }

    @Step("Check that user is logged in")
    public void checkLoggedIn() {
        avatar.shouldBe(visible);
    }

    @Step("To profile")
    public ProfilePage toProfile() {
        profileButton.click();
        return new ProfilePage();
    }

    @Step("Check avatar")
    public void checkAvatar(String avatarPath) {
        this.avatar
                .$(byTagName("img"))
                .should(imageCondition(avatarPath, true));
    }
}
