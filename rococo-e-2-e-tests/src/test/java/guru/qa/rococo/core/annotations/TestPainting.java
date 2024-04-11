package guru.qa.rococo.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface TestPainting {

    String title() default "";

    String description() default "";

    TestArtist artist();

    TestMuseum museum();
}
