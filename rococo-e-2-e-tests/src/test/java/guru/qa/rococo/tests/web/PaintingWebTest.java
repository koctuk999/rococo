package guru.qa.rococo.tests.web;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.*;
import guru.qa.rococo.db.model.PaintingEntity;
import guru.qa.rococo.db.repository.painting.PaintingRepository;
import guru.qa.rococo.db.repository.painting.PaintingRepositoryHibernate;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static guru.qa.rococo.core.TestTag.*;
import static guru.qa.rococo.page.component.message.SuccessMessage.PAINTING_ADDED;
import static guru.qa.rococo.page.component.message.SuccessMessage.PAINTING_UPDATED;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.*;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomTitle;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Painting web tests")
@Tags({
        @Tag(CLIENT_ACCEPTANCE),
        @Tag(PAINTING_ACCEPTANCE),
        @Tag(MUSEUM_ACCEPTANCE),
        @Tag(ARTIST_ACCEPTANCE)
})
public class PaintingWebTest extends BaseWebTest {

    private PaintingRepository paintingRepository = new PaintingRepositoryHibernate();

    @Test
    @DisplayName("Get painting")
    @GeneratedPainting(artist = @GeneratedArtist, museum = @GeneratedMuseum)
    public void getPainting(Painting painting) {
        mainPage
                .open()
                .toPaintingsPage()
                .clickPainting(painting.getTitle())
                .checkTitle(painting.getTitle())
                .checkDescription(painting.getDescription())
                .checkArtist(painting.getArtist().getName())
                .checkContent(painting.getContent(), false);

    }

    @Test
    @LoggedIn(user = @CreatedUser)
    @GeneratedArtist
    @GeneratedMuseum
    @DisplayName("Add painting")
    public void addPainting(Artist artist, Museum museum) {
        String title = genRandomTitle();
        String description = genRandomDescription(100);
        String content = PAINTING_PHOTO_PATH;

        mainPage
                .open()
                .toPaintingsPage()
                .addPainting()
                .setTitle(title)
                .setDescription(description)
                .setContent(content)
                .selectArtist(artist.getName())
                .selectMuseum(museum.getTitle())
                .submit()
                .checkSuccessMessage(PAINTING_ADDED, title);

        PaintingEntity paintingEntity = paintingRepository.findPaintingByTitle(title);
        check("expected title in db",
                paintingEntity.getTitle(), equalTo(title));
        check("expected description in db",
                paintingEntity.getDescription(), equalTo(description));
        check("expected content in db",
                new String(paintingEntity.getContent(), UTF_8),
                equalTo(getPhotoByPath(content)));
        check("expected artistId in db",
                paintingEntity.getArtistId().toString(), equalTo(artist.getId()));
        check("expected museumId in db",
                paintingEntity.getMuseumId().toString(), equalTo(museum.getId()));
    }

    @Test
    @GeneratedArtist
    @GeneratedMuseum
    @LoggedIn(user = @CreatedUser)
    @DisplayName("Add painting from artist page")
    public void addPaintingFromArtistPage(Artist artist, Museum museum) {
        String title = genRandomTitle();
        String description = genRandomDescription(100);
        String content = PAINTING_PHOTO_PATH;

        mainPage
                .open()
                .toArtistsPage()
                .clickArtist(artist.getName())
                .addPainting()
                .setTitle(title)
                .setDescription(description)
                .setContent(content)
                .selectMuseum(museum.getTitle())
                .submit()
                .checkSuccessMessage(PAINTING_ADDED, title);

        PaintingEntity paintingEntity = paintingRepository.findPaintingByTitle(title);
        check("expected title in db",
                paintingEntity.getTitle(), equalTo(title));
        check("expected description in db",
                paintingEntity.getDescription(), equalTo(description));
        check("expected content in db",
                new String(paintingEntity.getContent(), UTF_8),
                equalTo(getPhotoByPath(content)));
        check("expected artistId in db",
                paintingEntity.getArtistId().toString(), equalTo(artist.getId()));
        check("expected museumId in db",
                paintingEntity.getMuseumId().toString(), equalTo(museum.getId()));
    }

    @Test
    @LoggedIn(user = @CreatedUser)
    @GeneratedPainting(
            museum = @GeneratedMuseum,
            artist = @GeneratedArtist
    )
    @DisplayName("Update painting")
    public void updatePainting(Painting painting) {
        String newTitle = genRandomTitle();
        String newDescription = genRandomDescription(100);
        String newContent = PAINTING_NEW_PHOTO_PATH;

        mainPage
                .open()
                .toPaintingsPage()
                .clickPainting(painting.getTitle())
                .checkTitle(painting.getTitle())
                .checkDescription(painting.getDescription())
                .checkArtist(painting.getArtist().getName())
                .checkContent(painting.getContent(), false)
                .editPainting()
                .setTitle(newTitle)
                .setDescription(newDescription)
                .setContent(newContent)
                .submit()
                .checkSuccessMessage(PAINTING_UPDATED, newTitle);

        PaintingEntity paintingEntity = paintingRepository.findPaintingById(fromString(painting.getId()));
        check("expected new title in db",
                paintingEntity.getTitle(), equalTo(newTitle));
        check("expected new description in db",
                paintingEntity.getDescription(), equalTo(newDescription));
        check("expected new content in db",
                new String(paintingEntity.getContent(), UTF_8),
                equalTo(getPhotoByPath(newContent)));
        check("expected artistId in db",
                paintingEntity.getArtistId().toString(), equalTo(painting.getArtist().getId()));
        check("expected museumId in db",
                paintingEntity.getMuseumId().toString(), equalTo(painting.getMuseum().getId()));
    }

    @Test
    @DisplayName("Search painting")
    @GeneratedPainting(
            museum = @GeneratedMuseum,
            artist = @GeneratedArtist
    )
    public void searchPainting(Painting painting) {
        mainPage
                .open()
                .toPaintingsPage()
                .searchPainting(painting.getTitle())
                .checkPaintingSize(1)
                .checkPaintingInList(painting.getTitle());
    }
}
