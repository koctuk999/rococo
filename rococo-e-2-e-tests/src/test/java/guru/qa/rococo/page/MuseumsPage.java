package guru.qa.rococo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MuseumsPage extends BasePage<MuseumsPage> {

    private final SelenideElement searchInput = $("input[title='Искать музей...']");

    private final ElementsCollection museums = $$(By.className("qa-museum-item"));

    public Header getHeader() {
        return header;
    }

    @Override
    @Step("Wait for museum page loaded")
    public MuseumsPage waitForPageLoaded() {
        searchInput.should(visible);
        return this;
    }

    @Step("To museum")
    public MuseumPage clickMuseum(String museumTitle) {
        museums
                .findBy(text(museumTitle))
                .$(byAttribute("alt", museumTitle))
                .click();
        return new MuseumPage();
    }
}
