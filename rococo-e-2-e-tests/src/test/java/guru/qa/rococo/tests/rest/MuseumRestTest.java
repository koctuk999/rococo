package guru.qa.rococo.tests.rest;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.grpc.rococo.grpc.Country;
import guru.qa.grpc.rococo.grpc.Geo;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.core.annotations.Token;
import guru.qa.rococo.db.model.CountryEntity;
import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.repository.country.CountryRepository;
import guru.qa.rococo.db.repository.country.CountryRepositoryHibernate;
import guru.qa.rococo.db.repository.museum.MuseumRepository;
import guru.qa.rococo.db.repository.museum.MuseumRepositoryHibernate;
import guru.qa.rococo.tests.BaseRestTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.GATEWAY_ACCEPTANCE;
import static guru.qa.rococo.core.TestTag.MUSEUM_ACCEPTANCE;
import static guru.qa.rococo.db.model.MuseumEntity.toMuseumEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.Helper.toGrpc;
import static guru.qa.rococo.utils.Helper.toJson;
import static guru.qa.rococo.utils.ImageHelper.MUSEUM_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.*;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Museum rest tests")
@Tags({@Tag(GATEWAY_ACCEPTANCE), @Tag(MUSEUM_ACCEPTANCE)})
public class MuseumRestTest extends BaseRestTest {

    private final MuseumRepository museumRepository = new MuseumRepositoryHibernate();
    private final CountryRepository countryRepository = new CountryRepositoryHibernate();

    @Test
    @DisplayName("Add museum")
    @LoggedIn(user = @CreatedUser, setCookies = false)
    public void addMuseum(@Token String token) {
        String title = genRandomTitle();
        String photo = getPhotoByPath(MUSEUM_PHOTO_PATH);
        CountryEntity country = countryRepository.findAllCountries()
                .stream()
                .findFirst()
                .get();

        String city = genRandomCity();
        String description = genRandomDescription(100);

        Museum museum = Museum.newBuilder()
                .setTitle(title)
                .setPhoto(photo)
                .setDescription(description)
                .setGeo(
                        Geo.newBuilder()
                                .setCity(city)
                                .setCountry(
                                        Country.newBuilder()
                                                .setId(country.getId().toString())
                                                .setName(country.getCountryName())
                                                .build()
                                )
                                .build()
                )
                .build();

        JsonNode addedMuseum = step(
                "Add museum %s".formatted(title),
                () -> gatewayClient.addMuseum(toJson(museum), token)
        );

        MuseumEntity museumEntity = museumRepository.findMuseumByTitle(title);
        check("expected museum in db",
                museumEntity, equalTo(toMuseumEntity(toGrpc(addedMuseum, Museum.class))));

    }
}
