package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {
    public Header() {
        super($("header[id='shell-header']"));
    }

    private final SelenideElement title = $("a[href*='/']");

    @Step("Return to MainPage")
    public void toMainPage() {
        title.click();
    }
}
