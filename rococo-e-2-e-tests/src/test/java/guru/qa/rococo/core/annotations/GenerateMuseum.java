package guru.qa.rococo.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface GenerateMuseum {
    String title() default "";
    String description() default "";
    String city() default "";
    String countryName() default "";
}
