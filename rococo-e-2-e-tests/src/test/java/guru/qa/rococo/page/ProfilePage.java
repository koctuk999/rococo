package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.selenide.condition.ImageCondition.imageCondition;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class ProfilePage extends BasePage<ProfilePage> {
    private final SelenideElement pageTitle = $(byTagAndText("header", "Профиль"));
    private final SelenideElement username = $("h4[class='text-center']");
    private final SelenideElement avatar = $(byDataTestId("avatar")).$(byTagName("img"));

    private final SelenideElement avatarInput = $("input[type='file']");
    private final SelenideElement firstnameInput = $("input[name='firstname']");
    private final SelenideElement surnameInput = $("input[name='surname']");
    private final SelenideElement updateButton = $("button[type='submit']");
    private final SelenideElement logoutButton = $(byTagAndText("button", "Выйти"));
    private final SelenideElement closeButton = $(byTagAndText("button", "Закрыть"));

    @Override
    public ProfilePage waitForPageLoaded() {
        pageTitle.shouldBe(visible);
        return this;
    }

    @Step("Set avatar")
    public ProfilePage setAvatar(String imagePath) {
        avatarInput.uploadFromClasspath(imagePath);
        return this;
    }

    @Step("Set firstname {0}")
    public ProfilePage setFirstnameInput(String firstnameInput) {
        this.firstnameInput.setValue(firstnameInput);
        return this;
    }

    @Step("Set surname {0}")
    public ProfilePage setSurnameInput(String surnameInput) {
        this.surnameInput.setValue(surnameInput);
        return this;
    }

    @Step("Click update")
    public void update() {
        updateButton.click();
    }

    @Step("Click close")
    public void close() {
        closeButton.click();
    }

    @Step("Click logout")
    public void logout() {
        logoutButton.click();
    }

    @Step("Check username: {0}")
    public ProfilePage checkUsername(String username) {
        this.username.should(text(username));
        return this;
    }

    @Step("Check firstname: {0}")
    public ProfilePage checkFirstname(String firstname) {
        this.firstnameInput.shouldHave(value(firstname));
        return this;
    }

    @Step("Check firstname: {0}")
    public ProfilePage checkSurname(String surname) {
        this.surnameInput.shouldHave(value(surname));
        return this;
    }

    @Step("Check avatar")
    public ProfilePage checkAvatar(String avatarPath) {
        this.avatar.should(imageCondition(avatarPath, true));
        return this;
    }

}
