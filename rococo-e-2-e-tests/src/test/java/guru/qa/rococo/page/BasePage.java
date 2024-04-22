package guru.qa.rococo.page;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.CollectionElement;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.Header;
import guru.qa.rococo.page.component.message.ToastMessage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static guru.qa.rococo.config.Config.getInstance;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;
import static guru.qa.rococo.utils.Helper.waitFor;
import static java.lang.System.currentTimeMillis;

public abstract class BasePage<T extends BasePage> {
    static {
        Configuration.browserSize = "1980x1024";
    }

    protected static final Config CFG = getInstance();

    protected final Header header = new Header();

    protected final SelenideElement toaster = $(By.className("toast"));

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
    public T checkToasterMessage(ToastMessage msg) {
        toaster.should(visible).should(text(msg.getMessage()));
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
