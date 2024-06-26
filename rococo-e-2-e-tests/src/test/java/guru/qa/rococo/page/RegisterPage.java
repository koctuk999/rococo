package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");

    @Override
    public RegisterPage waitForPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        passwordSubmitInput.should(visible);
        return this;
    }

    @Step("Set username: {0}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Set password: {0}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Confirm password: {0}")
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    @Step("Submit register")
    public LoginPage successSubmit() {
        submitButton.click();
        return new LoginPage();
    }

    @Step("Submit register")
    public RegisterPage errorSubmit() {
        submitButton.click();
        return this;
    }

    @Step("Register user [username:{0}] [password:{1}]")
    public LoginPage registerUser(String username, String password) {
        setUsername(username);
        setPassword(password);
        setPasswordSubmit(password);
        successSubmit();
        return new LoginPage();
    }
}
