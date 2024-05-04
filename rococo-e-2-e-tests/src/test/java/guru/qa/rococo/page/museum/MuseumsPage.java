package guru.qa.rococo.page.museum;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import guru.qa.rococo.page.artist.ArtistUpsertModal;
import guru.qa.rococo.page.component.Header;
import guru.qa.rococo.page.component.SearchPlaceholder;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class MuseumsPage extends BasePage<MuseumsPage> {

    private final SearchPlaceholder searchPlaceholder = new SearchPlaceholder();

    private final SelenideElement addMuseumButton = $(byText("Добавить музей"));

    private final ElementsCollection museums = $$(byDataTestId("museum-item"));


    @Override
    public MuseumsPage waitForPageLoaded() {
        searchPlaceholder.getSelf().should(visible);
        return this;
    }

    public boolean isAddMuseumAvailable() {
        return addMuseumButton.exists();
    }

    @Step("To museum")
    public MuseumPage clickMuseum(String museumTitle) {
        scrollToElement($(byDataTestId("museum-items")).$(byText(museumTitle)), museums);
        museums
                .findBy(text(museumTitle))
                .$(byAttribute("alt", museumTitle))
                .click();
        return new MuseumPage().waitForPageLoaded();
    }

    @Step("Add museum")
    public MuseumUpsertModal addMuseum() {
        addMuseumButton.click();
        return new MuseumUpsertModal();
    }

    @Step("Search museum {0}")
    public MuseumsPage searchMuseum(String title) {
        searchPlaceholder.searchItem(title);
        return this;
    }

    @Step("Check museums size [expected {0}]")
    public MuseumsPage checkMuseumsSize(Integer expectedSize) {
        museums.should(CollectionCondition.size(expectedSize));
        return this;
    }

    @Step("Check museum {0} in list")
    public MuseumsPage checkMuseumInList(String museumTitle) {
        museums
                .findBy(text(museumTitle))
                .should(exist);
        return this;
    }
}
