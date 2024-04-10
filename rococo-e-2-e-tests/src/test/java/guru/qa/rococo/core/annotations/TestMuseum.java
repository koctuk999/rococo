package guru.qa.rococo.core.annotations;

import guru.qa.rococo.core.extensions.MuseumExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
@ExtendWith(MuseumExtension.class)
public @interface TestMuseum {
    String title() default "";
    String description() default "";
    String city() default "";
    String countryName() default "";
}
