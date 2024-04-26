package guru.qa.rococo.page;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.Header;
import guru.qa.rococo.page.component.message.Message;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.Nullable;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.config.Config.getInstance;
import static guru.qa.rococo.utils.Helper.waitFor;

public abstract class BasePage<T extends BasePage> {
    static {
        Configuration.browserSize = "1980x1024";
    }

    protected static final Config CFG = getInstance();

    protected final Header header = new Header();

    protected final SelenideElement toaster = $(By.className("toast"));
    protected final SelenideElement errorLabel = $(By.className("form__error"));

    public Header getHeader() {
        return header;
    }

    @Step("Scroll to element {0} in collection {1}")
    public <T> T scrollToElement(SelenideElement element, ElementsCollection collection) {
        waitFor(
                "scroll to element %s".formatted(element),
                15000,
                () -> {
                    collection.last().scrollIntoView(true);
                    return element.exists();
                }
        );
        element.shouldBe(visible);
        return (T) this;
    }

    @Step("Check that success message appears: {msg}")
    @SuppressWarnings("unchecked")
    public T checkSuccessMessage(Message msg, @Nullable String... formatArgs) {
        toaster
                .should(visible)
                .should(text(msg.getMessage().formatted(formatArgs)));
        return (T) this;
    }

    @Step("Check error message appears: {msg}")
    @SuppressWarnings("unchecked")
    public T checkErrorMessage(Message msg, @Nullable String... formatArgs) {
        errorLabel
                .should(visible)
                .should(text(msg.getMessage().formatted(formatArgs)));
        return (T) this;
    }

    @Step("Close toast")
    public T closeToast() {
        toaster.$(By.className("toast-actions")).click();
        return (T) this;
    }

    public LoginPage toLogin() {
        header.clickLoginButton();
        return new LoginPage();
    }

    public abstract T waitForPageLoaded();
}
