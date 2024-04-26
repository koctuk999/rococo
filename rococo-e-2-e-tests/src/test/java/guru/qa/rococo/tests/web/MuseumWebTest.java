package guru.qa.rococo.tests.web;

import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.core.annotations.GeneratedMuseum;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.db.model.CountryEntity;
import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.repository.country.CountryRepository;
import guru.qa.rococo.db.repository.country.CountryRepositoryHibernate;
import guru.qa.rococo.db.repository.museum.MuseumRepository;
import guru.qa.rococo.db.repository.museum.MuseumRepositoryHibernate;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static guru.qa.rococo.core.TestTag.*;
import static guru.qa.rococo.page.component.message.SuccessMessage.MUSEUM_ADDED;
import static guru.qa.rococo.page.component.message.SuccessMessage.MUSEUM_UPDATED;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.*;
import static guru.qa.rococo.utils.RandomUtils.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Museum web tests")
@Tags({@Tag(CLIENT_ACCEPTANCE), @Tag(MUSEUM_ACCEPTANCE)})
public class MuseumWebTest extends BaseWebTest {

    private CountryRepository countryRepository = new CountryRepositoryHibernate();
    private MuseumRepository museumRepository = new MuseumRepositoryHibernate();

    @Test
    @GeneratedMuseum
    @DisplayName("Get museum")
    public void getMuseum(Museum museum) {
        mainPage
                .open()
                .toMuseumsPage()
                .clickMuseum(museum.getTitle())
                .checkTitle(museum.getTitle())
                .checkDescription(museum.getDescription())
                .checkCountry(museum.getGeo().getCountry().getName())
                .checkCity(museum.getGeo().getCity())
                .checkPhoto(museum.getPhoto(), false);
    }

    @Test
    @LoggedIn(user = @CreatedUser)
    @DisplayName("Add museum")
    public void addMuseum() {
        String title = genRandomTitle();
        String photo = MUSEUM_PHOTO_PATH;

        CountryEntity country = countryRepository
                .findAllCountries()
                .stream()
                .findFirst()
                .get();

        String city = genRandomCity();
        String description = genRandomDescription(100);
        mainPage
                .open()
                .toMuseumsPage()
                .addMuseum()
                .setTitle(title)
                .setPhoto(photo)
                .selectCountry(country.getCountryName())
                .setCity(city)
                .setDescription(description)
                .submit()
                .checkSuccessMessage(MUSEUM_ADDED, title);

        MuseumEntity museumEntity = museumRepository.findMuseumByTitle(title);
        check("expected title in db",
                museumEntity.getTitle(), equalTo(title));
        check("expected country in db",
                museumEntity.getCountryId(), equalTo(country.getId()));
        check("expected city in db",
                museumEntity.getCity(), equalTo(city));
        check("expected description in db",
                museumEntity.getDescription(), equalTo(description));
        check("expected photo in db",
                new String(museumEntity.getPhoto(), UTF_8), equalTo(getPhotoByPath(photo)));

    }

    @Test
    @LoggedIn(user = @CreatedUser)
    @GeneratedMuseum
    @DisplayName("Update museum")
    public void updateMuseum(Museum museum) {
        String newTitle = genRandomTitle();
        String newPhoto = MUSEUM_NEW_PHOTO_PATH;

        CountryEntity newCountry = countryRepository
                .findAllCountries()
                .stream()
                .toList()
                .get(2);

        String newCity = genRandomCity();
        String newDescription = genRandomDescription(100);

        mainPage
                .open()
                .toMuseumsPage()
                .clickMuseum(museum.getTitle())
                .checkTitle(museum.getTitle())
                .checkDescription(museum.getDescription())
                .checkCountry(museum.getGeo().getCountry().getName())
                .checkCity(museum.getGeo().getCity())
                .checkPhoto(museum.getPhoto(), false)
                .editMuseum()
                .setTitle(newTitle)
                .setPhoto(newPhoto)
                .selectCountry(newCountry.getCountryName())
                .setCity(newCity)
                .setDescription(newDescription)
                .submit()
                .checkSuccessMessage(MUSEUM_UPDATED, newTitle);

        MuseumEntity museumEntity = museumRepository.findMuseumById(UUID.fromString(museum.getId()));
        check("expected new title in db",
                museumEntity.getTitle(), equalTo(newTitle));
        check("expected new country in db",
                museumEntity.getCountryId(), equalTo(newCountry.getId()));
        check("expected new city in db",
                museumEntity.getCity(), equalTo(newCity));
        check("expected new description in db",
                museumEntity.getDescription(), equalTo(newDescription));
        check("expected new photo in db",
                new String(museumEntity.getPhoto(), UTF_8), equalTo(getPhotoByPath(newPhoto)));
    }

    @Test
    @DisplayName("Search museum")
    @GeneratedMuseum
    public void searchMuseum(Museum museum) {
        mainPage
                .open()
                .toMuseumsPage()
                .searchMuseum(museum.getTitle())
                .checkMuseumsSize(1)
                .checkMuseumInList(museum.getTitle());
    }
}
