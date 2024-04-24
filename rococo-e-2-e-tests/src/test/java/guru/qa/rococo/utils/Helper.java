package guru.qa.rococo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.openqa.selenium.TimeoutException;

import java.util.function.Supplier;

import static io.qameta.allure.Allure.step;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;

public class Helper {

    public static String trimLongString(String str) {
        Integer maxLen = 255;
        return str.length() > maxLen
                ? "%s...".formatted(str.substring(0, maxLen))
                : str;
    }

    public static <T> T attempt(int maxRetries, long poolingInterval, Supplier<T> action) throws InterruptedException {
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

    public static void waitFor(
            String description,
            long timeout,
            Supplier<Boolean> action
    ) {
        long endTime = currentTimeMillis() + timeout;
        boolean condition = false;
        while (!condition) {
            Allure.step("Wait for %s".formatted(description));
            condition = action.get();
            if (currentTimeMillis() > endTime) {
                throw new TimeoutException("Did not wait for %s after %d ms".formatted(description, timeout));
            }
        }
    }

    @SneakyThrows
    public static JsonNode toJson(Message grpcObject) {
        ObjectMapper om = new ObjectMapper();
        return om.readTree(
                JsonFormat
                        .printer()
                        .print(grpcObject)
        );
    }

    @SneakyThrows
    public static <T extends Message> T toGrpc(JsonNode json, Class<T> messageClass) {
        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(json);
        T.Builder builder = (T.Builder) messageClass.getMethod("newBuilder").invoke(null);
        JsonFormat.parser().merge(jsonString, builder);
        return (T) builder.build();
    }
}

