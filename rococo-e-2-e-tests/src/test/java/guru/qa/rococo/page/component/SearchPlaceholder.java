package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class SearchPlaceholder extends BaseComponent<SearchPlaceholder> {
    public SearchPlaceholder() {
        super($(byDataTestId("search")));
    }

    private final SelenideElement searchInput = this.self.$(byTagName("input"));
    private final SelenideElement searchButton = this.self.$(byTagName("button"));

    @Step("Search {0}")
    public void searchItem(String item) {
        searchInput.setValue(item);
        searchButton.click();
    }
}
