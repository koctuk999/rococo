package guru.qa.rococo.utils;

import io.qameta.allure.Allure;
import org.hamcrest.Matcher;

import static guru.qa.rococo.utils.Helper.trimLongString;
import static org.hamcrest.MatcherAssert.assertThat;

public class CustomAssert {

    public static <T> void check(String description, T actual, Matcher<? super T> matcher) {
        Allure.step(
                "Check that, %s. \nExpected: %s \n Actual: %s "
                        .formatted(
                                description,
                                trimLongString(matcher.toString()),
                                trimLongString(actual.toString())
                        ),
                () -> assertThat(actual, matcher)
        );
    }
}
