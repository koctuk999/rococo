package guru.qa.rococo.core.extensions;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.api.grpc.GrpcArtistClient;
import guru.qa.rococo.core.annotations.TestArtist;
import guru.qa.rococo.core.annotations.TestPainting;
import guru.qa.rococo.utils.RandomUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.rococo.utils.RandomUtils.*;

public class ArtistExtension implements BeforeEachCallback, ParameterResolver {

    public static final Namespace ARTIST_NAMESPACE = Namespace.create(ArtistExtension.class);
    private GrpcArtistClient grpcArtistClient = new GrpcArtistClient();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        TestArtist annotationData = null;

        Optional<TestPainting> paintingAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), TestPainting.class);
        Optional<TestArtist> artistAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), TestArtist.class);

        if (paintingAnnotation.isPresent()) {
            annotationData = paintingAnnotation.get().artist();
        } else if (artistAnnotation.isPresent()) {
            annotationData = artistAnnotation.get();
        }

        if (annotationData != null) {
            String name = annotationData.name().isEmpty() ? genRandomName() : annotationData.name();
            String biography = annotationData.biography().isEmpty() ? genRandomDescription(50) : annotationData.biography();
            String photo = genRandomPhoto();
            Artist artist = Artist
                    .newBuilder()
                    .setName(name)
                    .setBiography(biography)
                    .setPhoto(photo)
                    .build();
            Artist addedArtist = grpcArtistClient.addArtist(artist);
            extensionContext
                    .getStore(ARTIST_NAMESPACE)
                    .put(extensionContext.getUniqueId(), addedArtist);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(Artist.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(ARTIST_NAMESPACE)
                .get(extensionContext.getUniqueId(), Artist.class);
    }
}
