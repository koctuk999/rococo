package guru.qa.rococo.page;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.Header;
import guru.qa.rococo.page.component.message.ToastMessage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.config.Config.getInstance;

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

    @Step("Check that success message appears: {msg}")
    @SuppressWarnings("unchecked")
    public T checkToasterMessage(ToastMessage msg) {
        toaster.should(visible).should(text(msg.getMessage()));
        return (T) this;
    }

    @Step("Close toast")
    public T closeToast(){
        toaster.$(By.className("toast-actions")).click();
        return (T) this;
    }

    public LoginPage toLogin() {
        header.clickLoginButton();
        return new LoginPage();
    }

    public abstract T waitForPageLoaded();
}
