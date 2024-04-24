package guru.qa.rococo.tests.rest;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.core.annotations.Token;
import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.db.model.ArtistEntity;
import guru.qa.rococo.db.repository.artist.ArtistRepository;
import guru.qa.rococo.db.repository.artist.ArtistRepositoryHibernate;
import guru.qa.rococo.tests.BaseRestTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.ARTIST_ACCEPTANCE;
import static guru.qa.rococo.core.TestTag.GATEWAY_ACCEPTANCE;
import static guru.qa.rococo.db.model.ArtistEntity.toArtistEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.Helper.toGrpc;
import static guru.qa.rococo.utils.Helper.toJson;
import static guru.qa.rococo.utils.ImageHelper.ARTIST_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomName;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Artist rest tests")
@Tags({@Tag(GATEWAY_ACCEPTANCE), @Tag(ARTIST_ACCEPTANCE)})
public class ArtistRestTest extends BaseRestTest {
    private final ArtistRepository artistRepository = new ArtistRepositoryHibernate();

    @Test
    @DisplayName("Add artist")
    @LoggedIn(user = @CreatedUser, setCookies = false)
    public void addArtist(@Token String token) {
        String photo = getPhotoByPath(ARTIST_PHOTO_PATH);
        String name = genRandomName();
        String biography = genRandomDescription(100);

        Artist artist = Artist.newBuilder()
                .setName(name)
                .setPhoto(photo)
                .setBiography(biography)
                .build();

        JsonNode addedArtist = step(
                "Add artist %s".formatted(name),
                () -> gatewayClient.addArtist(toJson(artist), token)
        );

        ArtistEntity artistEntity = artistRepository.findArtistByName(name);

        check("expected artist in db",
                artistEntity, equalTo(toArtistEntity(toGrpc(addedArtist, Artist.class))));
    }
}
