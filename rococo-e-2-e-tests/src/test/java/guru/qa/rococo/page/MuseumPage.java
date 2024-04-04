package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MuseumPage extends BasePage<MuseumPage> {

    private final Header header = new Header();

    private final SelenideElement searchInput = $("input[title='Искать музей...']");

    public Header getHeader() {
        return header;
    }

    @Override
    @Step("Wait for museum page loaded")
    public MuseumPage waitForPageLoaded() {
        searchInput.should(visible);
        return this;
    }
}
