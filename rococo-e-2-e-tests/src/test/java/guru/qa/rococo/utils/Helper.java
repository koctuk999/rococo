package guru.qa.rococo.utils;

import io.qameta.allure.Allure;
import jakarta.persistence.NoResultException;

import java.util.function.Supplier;

import static io.qameta.allure.Allure.step;
import static java.lang.Thread.sleep;

public class Helper {

    public static String trimLongString(String str) {
        Integer maxLen = 255;
        return str.length() > maxLen
                ? "%s...".formatted(str.substring(0, maxLen))
                : str;
    }

    public static <T> T retryAction(int maxRetries, long poolingInterval, Supplier<T> action) throws InterruptedException {
        int retries = 0;
        while (retries < maxRetries) {
            try {
                return step("Try run action [attempt %d]".formatted(retries), () -> action.get());
            } catch (Exception e) {
                retries++;
                if (retries < maxRetries) {
                    sleep(poolingInterval);
                }
            }
        }
        throw new IllegalStateException("Expected result was not received after %s attempts".formatted(maxRetries));
    }
}

