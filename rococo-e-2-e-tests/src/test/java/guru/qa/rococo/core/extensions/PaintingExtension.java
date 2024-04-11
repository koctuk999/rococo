package guru.qa.rococo.core.extensions;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.api.grpc.GrpcPaintingClient;
import guru.qa.rococo.core.annotations.TestPainting;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.rococo.core.extensions.ArtistExtension.ARTIST_NAMESPACE;
import static guru.qa.rococo.core.extensions.MuseumExtension.MUSEUM_NAMESPACE;
import static guru.qa.rococo.utils.RandomUtils.*;

public class PaintingExtension implements BeforeEachCallback, ParameterResolver {

    public static final Namespace PAINTING_NAMESPACE = Namespace.create(PaintingExtension.class);
    private final GrpcPaintingClient paintingClient = new GrpcPaintingClient();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<TestPainting> annotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), TestPainting.class);
        if (annotation.isPresent()) {
            TestPainting annotationsData = annotation.get();
            String title = annotationsData.title().isEmpty() ? genRandomTitle() : annotationsData.title();
            String description = annotationsData.description().isEmpty() ? genRandomDescription(50) : annotationsData.description();
            String content = genRandomPhoto();

            Artist artist = (Artist) extensionContext
                    .getStore(ARTIST_NAMESPACE)
                    .get(extensionContext.getUniqueId());

            Museum museum = (Museum) extensionContext
                    .getStore(MUSEUM_NAMESPACE)
                    .get(extensionContext.getUniqueId());

            Painting painting = Painting
                    .newBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setContent(content)
                    .setArtist(artist)
                    .setMuseum(museum)
                    .build();
            Painting addedPainting = paintingClient.addPainting(painting);

            extensionContext
                    .getStore(PAINTING_NAMESPACE)
                    .put(extensionContext.getUniqueId(), addedPainting);

        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(Painting.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(PAINTING_NAMESPACE)
                .get(extensionContext.getUniqueId(), Painting.class);
    }
}
