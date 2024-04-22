package guru.qa.rococo.tests.grpc;

import guru.qa.grpc.rococo.grpc.Country;
import guru.qa.grpc.rococo.grpc.Geo;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.db.model.CountryEntity;
import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.repository.museum.MuseumRepository;
import guru.qa.rococo.db.repository.museum.MuseumRepositoryHibernate;
import guru.qa.rococo.tests.BaseGrpcTest;
import guru.qa.rococo.utils.ImageHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.MUSEUM_ACCEPTANCE;
import static guru.qa.rococo.db.model.MuseumEntity.toMuseumEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.MUSEUM_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Museum grpc tests")
@Tag(MUSEUM_ACCEPTANCE)
public class MuseumGrpcTest extends BaseGrpcTest {
    private MuseumRepository museumRepository = new MuseumRepositoryHibernate();

    @Test
    @DisplayName("Add museum")
    public void addMuseum() {
        String title = genRandomTitle();
        String photo = getPhotoByPath(MUSEUM_PHOTO_PATH);
        Country country = grpcCountryClient.getCountries(1, null).getCountry(0);
        String city = genRandomCity();
        String description = genRandomDescription(100);

        Museum addedMuseum = grpcMuseumClient.addMuseum(
                Museum
                        .newBuilder()
                        .setTitle(title)
                        .setPhoto(photo)
                        .setDescription(description)
                        .setGeo(Geo
                                .newBuilder()
                                .setCity(city)
                                .setCountry(country)
                                .build()
                        )
                        .build()

        );

        MuseumEntity museumEntity = museumRepository.findMuseumByTitle(title);

        check("expected museum in db",
                museumEntity, equalTo(toMuseumEntity(addedMuseum)));

    }
}
