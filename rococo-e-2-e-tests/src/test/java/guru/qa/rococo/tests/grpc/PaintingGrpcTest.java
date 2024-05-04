package guru.qa.rococo.tests.grpc;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.GeneratedArtist;
import guru.qa.rococo.core.annotations.GeneratedMuseum;
import guru.qa.rococo.core.annotations.GeneratedPainting;
import guru.qa.rococo.db.model.PaintingEntity;
import guru.qa.rococo.db.repository.painting.PaintingRepository;
import guru.qa.rococo.db.repository.painting.PaintingRepositoryHibernate;
import guru.qa.rococo.tests.BaseGrpcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static guru.qa.rococo.core.TestTag.PAINTING_ACCEPTANCE;
import static guru.qa.rococo.db.model.PaintingEntity.toPaintingEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.*;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomTitle;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Painting grpc tests")
@Tag(PAINTING_ACCEPTANCE)
public class PaintingGrpcTest extends BaseGrpcTest {

    private PaintingRepository paintingRepository = new PaintingRepositoryHibernate();

    @Test
    @DisplayName("Get painting")
    @GeneratedPainting(
            museum = @GeneratedMuseum,
            artist = @GeneratedArtist
    )
    public void getPainting(Painting painting) {
        step("Check painting in list",
                () -> {
                    Optional<Painting> paintingInList = grpcPaintingClient.findPaintingInList(painting.getId());
                    check("list contain expected painting",
                            paintingInList.isPresent(), equalTo(true));
                    check("painting info is correct",
                            paintingInList.get(), equalTo(painting));
                }
        );

        step("Check painting by id", () -> {
            Painting paintingById = grpcPaintingClient.getPaintingById(painting.getId());
            check("painting info is correct",
                    paintingById, equalTo(painting));
        });

    }

    @Test
    @DisplayName("Add painting")
    @GeneratedArtist
    @GeneratedMuseum
    public void addPainting(Museum museum, Artist artist) {
        String title = genRandomTitle();
        String description = genRandomDescription(100);
        String content = getPhotoByPath(PAINTING_PHOTO_PATH);

        Painting addedPainting = step(
                "Add painting %s".formatted(title),
                () -> grpcPaintingClient.addPainting(
                        Painting
                                .newBuilder()
                                .setTitle(title)
                                .setDescription(description)
                                .setContent(content)
                                .setMuseum(museum)
                                .setArtist(artist)
                                .build()
                )
        );

        PaintingEntity paintingEntity = paintingRepository.findPaintingByTitle(title);

        check("expected painting in db",
                paintingEntity, equalTo(toPaintingEntity(addedPainting)));
    }

    @Test
    @DisplayName("Update painting")
    @GeneratedPainting(
            museum = @GeneratedMuseum,
            artist = @GeneratedArtist
    )
    public void updatePainting(Painting painting) {
        String newTitle = genRandomTitle();
        String newDescription = genRandomDescription(100);
        String newContent = getPhotoByPath(PAINTING_NEW_PHOTO_PATH);

        step("Check current info", () -> {
            Painting paintingById = grpcPaintingClient.getPaintingById(painting.getId());
            assertAll(
                    () -> check("expected title",
                            paintingById.getTitle(), equalTo(painting.getTitle())),
                    () -> check("expected content",
                            paintingById.getContent(), equalTo(painting.getContent())),
                    () -> check("expected description",
                            paintingById.getDescription(), equalTo(painting.getDescription()))
            );
        });

        step(
                "Update painting %s".formatted(painting.getId()),
                () -> grpcPaintingClient.updatePainting(
                        Painting
                                .newBuilder()
                                .setId(painting.getId())
                                .setTitle(newTitle)
                                .setDescription(newDescription)
                                .setContent(newContent)
                                .setMuseum(painting.getMuseum())
                                .setArtist(painting.getArtist())
                                .build()
                )
        );

        step("Check new info", () -> {
            Painting paintingById = grpcPaintingClient.getPaintingById(painting.getId());
            assertAll(
                    () -> check("expected title",
                            paintingById.getTitle(), equalTo(newTitle)),
                    () -> check("expected content",
                            paintingById.getContent(), equalTo(newContent)),
                    () -> check("expected description",
                            paintingById.getDescription(), equalTo(newDescription))
            );
        });
    }
}
