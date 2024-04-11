package guru.qa.rococo.core.extensions;

import guru.qa.grpc.rococo.grpc.Country;
import guru.qa.grpc.rococo.grpc.Geo;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.api.grpc.GrpcCountryClient;
import guru.qa.rococo.api.grpc.GrpcMuseumClient;
import guru.qa.rococo.core.annotations.TestMuseum;
import guru.qa.rococo.core.annotations.TestPainting;
import guru.qa.rococo.utils.RandomUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.rococo.utils.Helpers.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.*;

public class MuseumExtension implements BeforeEachCallback, ParameterResolver {

    public final static ExtensionContext.Namespace MUSEUM_NAMESPACE = ExtensionContext.Namespace.create(MuseumExtension.class);
    private GrpcMuseumClient museumClient = new GrpcMuseumClient();
    private GrpcCountryClient countryClient = new GrpcCountryClient();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        TestMuseum annotationData = null;
        Optional<TestPainting> paintingAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), TestPainting.class);
        Optional<TestMuseum> museumAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), TestMuseum.class);
        if (paintingAnnotation.isPresent()) {
            annotationData = paintingAnnotation.get().museum();
        } else if (museumAnnotation.isPresent()) {
            annotationData = museumAnnotation.get();
        }

        if (annotationData != null) {
            String title = annotationData.title().isEmpty() ? genRandomTitle() : annotationData.title();
            String description = annotationData.description().isEmpty() ? genRandomDescription(50) : annotationData.description();
            String city = annotationData.city().isEmpty() ? genRandomCity() : annotationData.city();

            String countryId = annotationData.countryName().isEmpty()
                    ? countryClient.getCountries(20, null).getCountry(0).getId()
                    : countryClient.getCountryByName(annotationData.countryName()).getId();

            String photo = genRandomPhoto();

            Museum museum = Museum
                    .newBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setGeo(
                            Geo
                                    .newBuilder()
                                    .setCountry(Country.newBuilder().setId(countryId).build())
                                    .setCity(city)
                                    .build()
                    )
                    .setPhoto(photo)
                    .build();
            Museum addedMuseum = museumClient.addMuseum(museum);
            extensionContext
                    .getStore(MUSEUM_NAMESPACE)
                    .put(extensionContext.getUniqueId(), addedMuseum);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(Museum.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(MUSEUM_NAMESPACE)
                .get(extensionContext.getUniqueId(), Museum.class);
    }
}
