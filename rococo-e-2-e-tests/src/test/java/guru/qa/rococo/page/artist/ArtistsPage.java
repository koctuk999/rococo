package guru.qa.rococo.page.artist;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.BasePage;
import guru.qa.rococo.page.component.Header;
import guru.qa.rococo.page.component.SearchPlaceholder;
import guru.qa.rococo.page.museum.MuseumsPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.rococo.selenide.selector.CustomSelectors.byDataTestId;

public class ArtistsPage extends BasePage<ArtistsPage> {

    private final SearchPlaceholder searchPlaceholder = new SearchPlaceholder();

    private final SelenideElement addArtistButton = $(byText("Добавить художника"));

    private final ElementsCollection artists = $$(byDataTestId("artist-item"));

    @Override
    public ArtistsPage waitForPageLoaded() {
        searchPlaceholder.getSelf().should(visible);
        return this;
    }

    public boolean isAddArtistAvailable(){
        return addArtistButton.exists();
    }

    @Step("Search artist {0}")
    public ArtistsPage searchArtist(String name){
        searchPlaceholder.searchItem(name);
        return this;
    }

    @Step("Check artists size [expected {0}]")
    public ArtistsPage checkArtistsSize(Integer expectedSize) {
        artists.should(CollectionCondition.size(expectedSize));
        return this;
    }

    @Step("Check artist {0} in list")
    public ArtistsPage checkArtistInList(String artistName) {
        artists
                .findBy(text(artistName))
                .should(exist);
        return this;
    }

    @Step("Click artist")
    public ArtistPage clickArtist(String artistName) {
        scrollToElement($(byDataTestId("artist-items")).$(byText(artistName)), artists);
        artists
                .findBy(text(artistName))
                .$(byClassName("avatar-image"))
                .click();
        return new ArtistPage().waitForPageLoaded();
    }

    @Step("Add artist")
    public ArtistUpsertModal addArtist() {
        addArtistButton.click();
        return new ArtistUpsertModal();
    }
}
