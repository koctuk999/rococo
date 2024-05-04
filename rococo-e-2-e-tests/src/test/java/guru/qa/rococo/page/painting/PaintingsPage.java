package guru.qa.rococo.page.painting;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import guru.qa.rococo.page.component.Header;
import guru.qa.rococo.page.component.SearchPlaceholder;
import guru.qa.rococo.page.museum.MuseumUpsertModal;
import guru.qa.rococo.page.museum.MuseumsPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class PaintingsPage extends BasePage<PaintingsPage> {

    private final SearchPlaceholder searchPlaceholder = new SearchPlaceholder();
    private final SelenideElement addPaintingButton = $(byText("Добавить картину"));

    private final ElementsCollection paintings = $$(byDataTestId("painting-item"));

    @Override
    public PaintingsPage waitForPageLoaded() {
        searchPlaceholder.getSelf().should(visible);
        return this;
    }

    public boolean isAddPaintingAvailable(){
        return addPaintingButton.exists();
    }


    @Step("Search painting {0}")
    public PaintingsPage searchPainting(String title) {
        searchPlaceholder.searchItem(title);
        return this;
    }


    @Step("Check paintings size [expected {0}]")
    public PaintingsPage checkPaintingSize(Integer expectedSize) {
        paintings.should(CollectionCondition.size(expectedSize));
        return this;
    }

    @Step("Check painting {0} in list")
    public PaintingsPage checkPaintingInList(String paintingTitle) {
        paintings
                .findBy(text(paintingTitle))
                .should(exist);
        return this;
    }

    @Step("Click painting")
    public PaintingPage clickPainting(String paintingTitle) {
        scrollToElement($(byDataTestId("painting-items")).$(byText(paintingTitle)), paintings);
        paintings
                .findBy(text(paintingTitle))
                .$(byAttribute("alt", paintingTitle))
                .click();
        return new PaintingPage().waitForPageLoaded();
    }

    @Step("Add painting")
    public PaintingUpsertModal addPainting() {
        addPaintingButton.click();
        return new PaintingUpsertModal();
    }
}
