package guru.qa.rococo.tests.grpc;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.core.TestTag;
import guru.qa.rococo.db.model.ArtistEntity;
import guru.qa.rococo.db.repository.artist.ArtistRepository;
import guru.qa.rococo.db.repository.artist.ArtistRepositoryHibernate;
import guru.qa.rococo.tests.BaseGrpcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.ARTIST_ACCEPTANCE;
import static guru.qa.rococo.db.model.ArtistEntity.toArtistEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.ARTIST_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomName;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Artist grpc tests")
@Tag(ARTIST_ACCEPTANCE)
public class ArtistGrpcTest extends BaseGrpcTest {

    private final ArtistRepository artistRepository = new ArtistRepositoryHibernate();

    @Test
    @DisplayName("Add artist")
    public void addArtist() {
        String photo = getPhotoByPath(ARTIST_PHOTO_PATH);
        String name = genRandomName();
        String biography = genRandomDescription(100);

        Artist addedArtist = grpcArtistClient.addArtist(
                Artist
                        .newBuilder()
                        .setName(name)
                        .setPhoto(photo)
                        .setBiography(biography)
                        .build()
        );

        ArtistEntity artistEntity = artistRepository.findArtistByName(name);
        check("expected artist in db",
                artistEntity, equalTo(toArtistEntity(addedArtist)));
    }
}
