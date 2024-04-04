package guru.qa.rococo.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PaintingPage extends BasePage<PaintingPage> {
    private final Header header = new Header();

    private final SelenideElement searchInput = $("input[title='Искать картины...']");

    public Header getHeader() {
        return header;
    }

    @Override
    @Step("Wait for painting page loaded")
    public PaintingPage waitForPageLoaded() {
        searchInput.should(visible);
        return this;
    }
}
