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
import guru.qa.rococo.page.museum.MuseumsPage;
import guru.qa.rococo.tests.BaseWebTest;
import guru.qa.rococo.utils.ImageHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static guru.qa.rococo.core.TestTag.*;
import static guru.qa.rococo.core.TestTag.ARTIST_ACCEPTANCE;
import static guru.qa.rococo.page.component.message.SuccessMessage.MUSEUM_ADDED;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.MUSEUM_PHOTO_PATH;
import static guru.qa.rococo.utils.RandomUtils.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Museum tests")
@Tags({@Tag(CLIENT_ACCEPTANCE), @Tag(MUSEUM_ACCEPTANCE)})
public class MuseumTest extends BaseWebTest {

    CountryRepository countryRepository = new CountryRepositoryHibernate();
    MuseumRepository museumRepository = new MuseumRepositoryHibernate();

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
                .checkToasterMessage(MUSEUM_ADDED);

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
                new String(museumEntity.getPhoto(), UTF_8), equalTo(ImageHelper.getPhotoByPath(photo)));

    }
}
