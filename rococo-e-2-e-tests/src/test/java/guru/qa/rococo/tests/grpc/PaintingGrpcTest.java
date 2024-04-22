package guru.qa.rococo.tests.grpc;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.core.annotations.GeneratedArtist;
import guru.qa.rococo.core.annotations.GeneratedMuseum;
import guru.qa.rococo.db.model.PaintingEntity;
import guru.qa.rococo.db.repository.painting.PaintingRepository;
import guru.qa.rococo.db.repository.painting.PaintingRepositoryHibernate;
import guru.qa.rococo.tests.BaseGrpcTest;
import guru.qa.rococo.utils.ImageHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.PAINTING_ACCEPTANCE;
import static guru.qa.rococo.db.model.PaintingEntity.toPaintingEntity;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.ImageHelper.PAINTING_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.genRandomDescription;
import static guru.qa.rococo.utils.RandomUtils.genRandomTitle;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Painting grpc tests")
@Tag(PAINTING_ACCEPTANCE)
public class PaintingGrpcTest extends BaseGrpcTest {

    private PaintingRepository paintingRepository = new PaintingRepositoryHibernate();

    @Test
    @DisplayName("Add painting")
    @GeneratedArtist
    @GeneratedMuseum
    public void addPainting(Museum museum, Artist artist) {
        String title = genRandomTitle();
        String description = genRandomDescription(100);
        String content = getPhotoByPath(PAINTING_PHOTO_PATH);

        Painting addedPainting = grpcPaintingClient.addPainting(
                Painting
                        .newBuilder()
                        .setTitle(title)
                        .setDescription(description)
                        .setContent(content)
                        .setMuseum(museum)
                        .setArtist(artist)
                        .build()
        );

        PaintingEntity paintingEntity = paintingRepository.findPaintingByTitle(title);

        check("expected painting in db",
                paintingEntity, equalTo(toPaintingEntity(addedPainting)));
    }
}
