package guru.qa.rococo.core.extensions;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.api.grpc.GrpcArtistClient;
import guru.qa.rococo.core.annotations.TestArtist;
import guru.qa.rococo.core.annotations.TestPainting;
import guru.qa.rococo.utils.ImageHelper;
import guru.qa.rococo.utils.RandomUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.rococo.utils.ImageHelper.ARTIST_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.*;
import static io.qameta.allure.Allure.step;

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
            TestArtist finalAnnotationData = annotationData;
            step("Precondition step: generate Artist", () -> {
                String name = finalAnnotationData.name().isEmpty() ? genRandomName() : finalAnnotationData.name();
                String biography = finalAnnotationData.biography().isEmpty() ? genRandomDescription(50) : finalAnnotationData.biography();
                String photo = getPhotoByPath(ARTIST_PHOTO_PATH);
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
            });
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
