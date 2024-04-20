package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $("a[href='/register']");

    @Override
    public LoginPage waitForPageLoaded() {
        usernameInput.shouldBe(visible);
        return this;
    }

    @Step("Set username: {0}")
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Set password: {0}")
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Submit login")
    public MainPage successSubmit() {
        submitButton.click();
        return new MainPage();
    }

    @Step("Submit login")
    public LoginPage errorSubmit() {
        submitButton.click();
        return this;
    }

    @Step("Sign in [login: {0}] [password: {1}]")
    public void signIn(String username, String password){
        setUsername(username);
        setPassword(password);
        successSubmit();
    }

    @Step("Register click")
    public RegisterPage toRegister(){
        registerButton.click();
        return new RegisterPage();
    }
}
