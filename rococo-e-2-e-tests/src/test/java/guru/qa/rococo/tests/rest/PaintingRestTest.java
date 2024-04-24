package guru.qa.rococo.tests.rest;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.*;
import guru.qa.rococo.db.model.PaintingEntity;
import guru.qa.rococo.db.repository.painting.PaintingRepository;
import guru.qa.rococo.db.repository.painting.PaintingRepositoryHibernate;
import guru.qa.rococo.tests.BaseRestTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.GATEWAY_ACCEPTANCE;
import static guru.qa.rococo.core.TestTag.PAINTING_ACCEPTANCE;
import static guru.qa.rococo.db.model.PaintingEntity.toPaintingEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.Helper.toGrpc;
import static guru.qa.rococo.utils.Helper.toJson;
import static guru.qa.rococo.utils.ImageHelper.PAINTING_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomTitle;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Painting rest tests")
@Tags({@Tag(GATEWAY_ACCEPTANCE), @Tag(PAINTING_ACCEPTANCE)})
public class PaintingRestTest extends BaseRestTest {

    private final PaintingRepository paintingRepository = new PaintingRepositoryHibernate();

    @Test
    @DisplayName("Add painting")
    @LoggedIn(user = @CreatedUser, setCookies = false)
    @GeneratedArtist
    @GeneratedMuseum
    public void addPainting(
            @Token String token,
            Museum museum,
            Artist artist
    ) {

        String title = genRandomTitle();
        String description = genRandomDescription(100);
        String content = getPhotoByPath(PAINTING_PHOTO_PATH);

        Painting painting = Painting
                .newBuilder()
                .setTitle(title)
                .setDescription(description)
                .setContent(content)
                .setMuseum(museum)
                .setArtist(artist)
                .build();

        JsonNode addedPainting = step(
                "Add painting %s".formatted(title),
                () -> gatewayClient.addPainting(toJson(painting), token)
        );

        PaintingEntity paintingEntity = paintingRepository.findPaintingByTitle(title);

        check("expected painting in db",
                paintingEntity, equalTo(toPaintingEntity(toGrpc(addedPainting, Painting.class))));

    }
}
