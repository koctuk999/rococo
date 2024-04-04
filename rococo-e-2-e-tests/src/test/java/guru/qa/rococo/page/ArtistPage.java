package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.BaseComponent;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ArtistPage extends BasePage<ArtistPage> {
    private final Header header = new Header();
    private final SelenideElement searchInput = $("input[title='Искать художников...']");

    public Header getHeader() {
        return header;
    }

    @Override
    @Step("Wait for artist page loaded")
    public ArtistPage waitForPageLoaded() {
        searchInput.should(visible);
        return this;
    }
}
