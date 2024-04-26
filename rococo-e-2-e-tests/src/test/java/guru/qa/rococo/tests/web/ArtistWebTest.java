package guru.qa.rococo.tests.web;


import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.*;
import guru.qa.rococo.db.model.ArtistEntity;
import guru.qa.rococo.db.repository.artist.ArtistRepository;
import guru.qa.rococo.db.repository.artist.ArtistRepositoryHibernate;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.*;
import static guru.qa.rococo.core.TestTag.ARTIST_ACCEPTANCE;
import static guru.qa.rococo.page.component.message.SuccessMessage.ARTIST_ADDED;
import static guru.qa.rococo.page.component.message.SuccessMessage.ARTIST_UPDATED;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.*;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomName;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Artist web tests")
@Tags({@Tag(CLIENT_ACCEPTANCE), @Tag(ARTIST_ACCEPTANCE)})
public class ArtistWebTest extends BaseWebTest {
    private ArtistRepository artistRepository = new ArtistRepositoryHibernate();

    @Test
    @GeneratedArtist
    @DisplayName("Get artist")
    public void getArtist(Artist artist) {
        mainPage
                .open()
                .toArtistsPage()
                .clickArtist(artist.getName())
                .checkName(artist.getName())
                .checkBiography(artist.getBiography())
                .checkPhoto(artist.getPhoto(), false);
    }

    @Test
    @GeneratedPainting(
            museum = @GeneratedMuseum,
            artist = @GeneratedArtist
    )
    @DisplayName("Check painting by artist")
    public void checkPaintingByArtist(Artist artist, Painting painting) {
        mainPage
                .open()
                .toArtistsPage()
                .clickArtist(artist.getName())
                .checkPaintingInList(painting.getTitle());
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
                .checkSuccessMessage(ARTIST_ADDED, name);

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

    @Test
    @DisplayName("Update artist")
    @LoggedIn(user = @CreatedUser)
    @GeneratedArtist
    public void updateArtist(Artist artist) {
        String newPhoto = ARTIST_NEW_PHOTO_PATH;
        String newName = genRandomName();
        String newBiography = genRandomDescription(100);

        mainPage
                .open()
                .toArtistsPage()
                .clickArtist(artist.getName())
                .checkName(artist.getName())
                .checkBiography(artist.getBiography())
                .checkPhoto(artist.getPhoto(), false)
                .editArtist()
                .setName(newName)
                .setBiography(newBiography)
                .setPhoto(newPhoto)
                .submit()
                .checkSuccessMessage(ARTIST_UPDATED, newName);

        ArtistEntity artistEntity = artistRepository.findArtistById(fromString(artist.getId()));
        check("expected new name in db",
                artistEntity.getName(), equalTo(newName));
        check("expected new biography in db",
                artistEntity.getBiography(), equalTo(newBiography));
        check("expected new photo in db",
                new String(artistEntity.getPhoto(), UTF_8),
                equalTo(getPhotoByPath(newPhoto))
        );
    }

    @Test
    @GeneratedArtist
    @DisplayName("Search artist")
    public void searchArtist(Artist artist) {
        mainPage
                .open()
                .toArtistsPage()
                .searchArtist(artist.getName())
                .checkArtistsSize(1)
                .checkArtistInList(artist.getName());

    }
}
