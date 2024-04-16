package guru.qa.rococo.core.extensions;

import guru.qa.grpc.rococo.grpc.Country;
import guru.qa.grpc.rococo.grpc.Geo;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.api.grpc.GrpcCountryClient;
import guru.qa.rococo.api.grpc.GrpcMuseumClient;
import guru.qa.rococo.core.annotations.GenerateMuseum;
import guru.qa.rococo.core.annotations.GeneratePainting;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.rococo.utils.ImageHelper.MUSEUM_PHOTO_PATH;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;
import static guru.qa.rococo.utils.RandomUtils.*;
import static io.qameta.allure.Allure.step;

public class MuseumExtension implements BeforeEachCallback, ParameterResolver {

    public final static ExtensionContext.Namespace MUSEUM_NAMESPACE = ExtensionContext.Namespace.create(MuseumExtension.class);
    private GrpcMuseumClient museumClient = new GrpcMuseumClient();
    private GrpcCountryClient countryClient = new GrpcCountryClient();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        GenerateMuseum annotationData = null;
        Optional<GeneratePainting> paintingAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), GeneratePainting.class);
        Optional<GenerateMuseum> museumAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), GenerateMuseum.class);

        if (paintingAnnotation.isPresent()) {
            annotationData = paintingAnnotation.get().museum();
        } else if (museumAnnotation.isPresent()) {
            annotationData = museumAnnotation.get();
        }

        if (annotationData != null) {
            GenerateMuseum finalAnnotationData = annotationData;
            step("Precondition step: generate Museum", () -> {
                String title = finalAnnotationData.title().isEmpty() ? genRandomTitle() : finalAnnotationData.title();
                String description = finalAnnotationData.description().isEmpty() ? genRandomDescription(50) : finalAnnotationData.description();
                String city = finalAnnotationData.city().isEmpty() ? genRandomCity() : finalAnnotationData.city();

                String countryId = finalAnnotationData.countryName().isEmpty()
                        ? countryClient.getCountries(20, null).getCountry(0).getId()
                        : countryClient.getCountryByName(finalAnnotationData.countryName()).getId();

                String photo = getPhotoByPath(MUSEUM_PHOTO_PATH);

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
            });
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
