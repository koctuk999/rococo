package guru.qa.rococo.tests.grpc;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.core.annotations.GeneratedArtist;
import guru.qa.rococo.db.model.ArtistEntity;
import guru.qa.rococo.db.repository.artist.ArtistRepository;
import guru.qa.rococo.db.repository.artist.ArtistRepositoryHibernate;
import guru.qa.rococo.tests.BaseGrpcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static guru.qa.rococo.core.TestTag.ARTIST_ACCEPTANCE;
import static guru.qa.rococo.db.model.ArtistEntity.toArtistEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.*;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomName;
import static io.qameta.allure.Allure.step;
import static java.lang.Thread.sleep;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Artist grpc tests")
@Tag(ARTIST_ACCEPTANCE)
public class ArtistGrpcTest extends BaseGrpcTest {

    private final ArtistRepository artistRepository = new ArtistRepositoryHibernate();

    @Test
    @DisplayName("Get artist")
    @GeneratedArtist
    public void getArtist(Artist artist) {
        step("Check artist in list", () -> {
                    Optional<Artist> artistInList = grpcArtistClient.findArtistInList(artist.getId());

                    check("list contains expected artist",
                            artistInList.isPresent(), equalTo(true));
                    check("artist info is correct",
                            artistInList.get(), equalTo(artist));
                }
        );

        step("Check artist by id", () -> {
            Artist artistById = grpcArtistClient.getArtistById(artist.getId());
            check("artist info is correct",
                    artistById, equalTo(artist));
        });
    }

    @Test
    @DisplayName("Add artist")
    public void addArtist() {
        String photo = getPhotoByPath(ARTIST_PHOTO_PATH);
        String name = genRandomName();
        String biography = genRandomDescription(100);

        Artist addedArtist = step(
                "Add artist %s".formatted(name),
                () -> grpcArtistClient.addArtist(
                        Artist
                                .newBuilder()
                                .setName(name)
                                .setPhoto(photo)
                                .setBiography(biography)
                                .build()
                )
        );

        ArtistEntity artistEntity = artistRepository.findArtistByName(name);
        check("expected artist in db",
                artistEntity, equalTo(toArtistEntity(addedArtist)));
    }

    @Test
    @DisplayName("Update artist")
    @GeneratedArtist
    public void updateArtist(Artist artist) {
        String newPhoto = getPhotoByPath(ARTIST_NEW_PHOTO_PATH);
        String newName = genRandomName();
        String newBiography = genRandomDescription(100);

        step("Check current info", () -> {
            Artist artistById = grpcArtistClient.getArtistById(artist.getId());
            assertAll(
                    () -> check("expected photo",
                            artistById.getPhoto(), equalTo(artist.getPhoto())),
                    () -> check("expected name",
                            artistById.getName(), equalTo(artist.getName())),
                    () -> check("expected biography",
                            artistById.getBiography(), equalTo(artist.getBiography()))
            );
        });

        step("Update artist %s".formatted(artist.getId()),
                () -> grpcArtistClient.updateArtist(
                        Artist
                                .newBuilder()
                                .setId(artist.getId())
                                .setName(newName)
                                .setPhoto(newPhoto)
                                .setBiography(newBiography)
                                .build()
                )
        );

        step("Check new info", () -> {
            Artist artistById = grpcArtistClient.getArtistById(artist.getId());
            assertAll(
                    () -> check("expected photo",
                            artistById.getPhoto(), equalTo(newPhoto)),
                    () -> check("expected name",
                            artistById.getName(), equalTo(newName)),
                    () -> check("expected biography",
                            artistById.getBiography(), equalTo(newBiography))
            );
        });
    }
}
