package guru.qa.rococo.page;

import com.codeborne.selenide.Configuration;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import static guru.qa.rococo.config.Config.getInstance;

public abstract class BasePage<T extends BasePage> {
    static {
        Configuration.browserSize = "1980x1024";
    }

    protected static final Config CFG = getInstance();

    protected final Header header = new Header();

    public abstract T waitForPageLoaded();
}
