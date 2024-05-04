package guru.qa.rococo.core.annotations;

import guru.qa.rococo.core.extensions.ArtistExtension;
import guru.qa.rococo.core.extensions.ContextHolderExtension;
import guru.qa.rococo.core.extensions.MuseumExtension;
import guru.qa.rococo.core.extensions.PaintingExtension;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static guru.qa.rococo.core.TestTag.GRPC_API;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        ContextHolderExtension.class,
        AllureJunit5.class,
        MuseumExtension.class,
        ArtistExtension.class,
        PaintingExtension.class
})
@Tag(GRPC_API)
public @interface GrpcTest {
}
