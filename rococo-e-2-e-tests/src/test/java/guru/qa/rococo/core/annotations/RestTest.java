package guru.qa.rococo.core.annotations;

import guru.qa.rococo.core.extensions.*;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static guru.qa.rococo.core.TestTag.REST_API;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@ExtendWith({
        ContextHolderExtension.class,
        AllureJunit5.class,
        BrowserExtension.class,
        CreateUserExtension.class,
        ApiLoginExtension.class,
        MuseumExtension.class,
        ArtistExtension.class,
        PaintingExtension.class
})
@Tag(REST_API)
public @interface RestTest {
}
