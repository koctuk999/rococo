package guru.qa.rococo.tests.grpc;

import guru.qa.grpc.rococo.grpc.Country;
import guru.qa.grpc.rococo.grpc.Geo;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.core.annotations.GeneratedMuseum;
import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.repository.museum.MuseumRepository;
import guru.qa.rococo.db.repository.museum.MuseumRepositoryHibernate;
import guru.qa.rococo.tests.BaseGrpcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static guru.qa.rococo.core.TestTag.MUSEUM_ACCEPTANCE;
import static guru.qa.rococo.db.model.MuseumEntity.toMuseumEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.*;
import static guru.qa.rococo.utils.RandomUtils.*;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Museum grpc tests")
@Tag(MUSEUM_ACCEPTANCE)
public class MuseumGrpcTest extends BaseGrpcTest {
    private MuseumRepository museumRepository = new MuseumRepositoryHibernate();

    @Test
    @DisplayName("Get museum")
    @GeneratedMuseum
    public void getMuseum(Museum museum) {
        step("Check museum in list",
                () -> {
                    Optional<Museum> museumInList = grpcMuseumClient.findMuseumInList(museum.getId());
                    check("list contain expected museum",
                            museumInList.isPresent(), equalTo(true));
                    check("museum info is correct",
                            museumInList.get(), equalTo(museum));
                }
        );

        step("Check museum by id", () -> {
            Museum museumById = grpcMuseumClient.getMuseumById(museum.getId());
            check("museum info is correct",
                    museumById, equalTo(museum));
        });
    }

    @Test
    @DisplayName("Add museum")
    public void addMuseum() {
        String title = genRandomTitle();
        String photo = getPhotoByPath(MUSEUM_PHOTO_PATH);
        Country country = grpcCountryClient.getCountries(1, null).getCountry(0);
        String city = genRandomCity();
        String description = genRandomDescription(100);

        Museum addedMuseum = step(
                "Add museum %s".formatted(title),
                () -> grpcMuseumClient.addMuseum(
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

                )
        );

        MuseumEntity museumEntity = museumRepository.findMuseumByTitle(title);

        check("expected museum in db",
                museumEntity, equalTo(toMuseumEntity(addedMuseum)));

    }

    @Test
    @DisplayName("Update museum")
    @GeneratedMuseum
    public void updateMuseum(Museum museum) {
        String newTitle = genRandomTitle();
        String newPhoto = getPhotoByPath(MUSEUM_NEW_PHOTO_PATH);
        Country newCountry = grpcCountryClient.getCountries(2, null).getCountry(0);
        String newCity = genRandomCity();
        String newDescription = genRandomDescription(100);

        step("Check current info", () -> {
            Museum museumById = grpcMuseumClient.getMuseumById(museum.getId());
            assertAll(
                    () -> check("expected title",
                            museumById.getTitle(), equalTo(museum.getTitle())),
                    () -> check("expected photo",
                            museumById.getPhoto(), equalTo(museum.getPhoto())),
                    () -> check("expected country",
                            museumById.getGeo().getCountry(), equalTo(museum.getGeo().getCountry())),
                    () -> check("expected city",
                            museumById.getGeo().getCity(), equalTo(museum.getGeo().getCity())),
                    () -> check("expected description",
                            museumById.getDescription(), equalTo(museum.getDescription()))
            );
        });
        step(
                "Update museum %s".formatted(museum.getId()),
                () -> grpcMuseumClient.updateMuseum(
                        Museum
                                .newBuilder()
                                .setId(museum.getId())
                                .setTitle(newTitle)
                                .setPhoto(newPhoto)
                                .setDescription(newDescription)
                                .setGeo(Geo
                                        .newBuilder()
                                        .setCity(newCity)
                                        .setCountry(newCountry)
                                        .build()
                                )
                                .build()

                )
        );

        step("Check new info", () -> {
            Museum museumById = grpcMuseumClient.getMuseumById(museum.getId());
            assertAll(
                    () -> check("expected title",
                            museumById.getTitle(), equalTo(newTitle)),
                    () -> check("expected photo",
                            museumById.getPhoto(), equalTo(newPhoto)),
                    () -> check("expected country",
                            museumById.getGeo().getCountry(), equalTo(newCountry)),
                    () -> check("expected city",
                            museumById.getGeo().getCity(), equalTo(newCity)),
                    () -> check("expected description",
                            museumById.getDescription(), equalTo(newDescription))
            );
        });
    }
}
