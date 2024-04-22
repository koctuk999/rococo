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

import static guru.qa.rococo.core.TestTag.*;
import static guru.qa.rococo.page.component.message.SuccessMessage.PAINTING_ADDED;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.PAINTING_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomTitle;
import static java.nio.charset.StandardCharsets.UTF_8;
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
                .checkToasterMessage(PAINTING_ADDED);

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
}
