package guru.qa.rococo.tests.web;


import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.core.annotations.GeneratedArtist;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.db.model.ArtistEntity;
import guru.qa.rococo.db.repository.artist.ArtistRepository;
import guru.qa.rococo.db.repository.artist.ArtistRepositoryHibernate;
import guru.qa.rococo.page.artist.ArtistPage;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.*;
import static guru.qa.rococo.core.TestTag.ARTIST_ACCEPTANCE;
import static guru.qa.rococo.page.component.message.SuccessMessage.ARTIST_ADDED;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.ARTIST_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomName;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Artist tests")
@Tags({@Tag(CLIENT_ACCEPTANCE), @Tag(ARTIST_ACCEPTANCE)})
public class ArtistTest extends BaseWebTest {
    ArtistRepository artistRepository = new ArtistRepositoryHibernate();

    @Test
    @GeneratedArtist
    @DisplayName("Get artist")
    public void getArtist(Artist artist) {
        ArtistPage artistPage = mainPage
                .open()
                .toArtistsPage()
                .clickArtist(artist.getName())
                .checkName(artist.getName())
                .checkBiography(artist.getBiography())
                .checkPhoto(artist.getPhoto(), false);
    }

    @Test
    @DisplayName("Add artist")
    @LoggedIn(user = @CreatedUser)
    public void addArtist() {
        String photo = ARTIST_PHOTO_PATH;
        String name = genRandomName();
        String biography = genRandomDescription(100);

        mainPage
                .open()
                .toArtistsPage()
                .addArtist()
                .setName(name)
                .setBiography(biography)
                .setPhoto(photo)
                .submit()
                .checkToasterMessage(ARTIST_ADDED);

        ArtistEntity artistEntity = artistRepository.findArtistByName(name);
        check("expected name in db",
                artistEntity.getName(), equalTo(name));
        check("expected biography in db",
                artistEntity.getBiography(), equalTo(biography));
        check("expected photo in db",
                new String(artistEntity.getPhoto(), UTF_8),
                equalTo(getPhotoByPath(photo))
        );
    }
}
