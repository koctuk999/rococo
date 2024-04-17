package guru.qa.rococo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.className;

public class PaintingsPage extends BasePage<PaintingsPage> {

    private final SelenideElement searchInput = $("input[title='Искать картины...']");

    private final ElementsCollection paintings = $$(className("qa-painting-item"));

    public Header getHeader() {
        return header;
    }

    @Override
    public PaintingsPage waitForPageLoaded() {
        searchInput.should(visible);
        return this;
    }

    @Step("Click painting")
    public PaintingPage clickPainting(String paintingTitle) {
        paintings
                .findBy(text(paintingTitle))
                .$(byAttribute("alt", paintingTitle))
                .click();
        return new PaintingPage();
    }
}
