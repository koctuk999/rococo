package guru.qa.rococo.utils;

public class Helper {

    public static String trimLongString(String str) {
        Integer maxLen = 255;
        return str.length() > maxLen
                ? "%s...".formatted(str.substring(0, maxLen))
                : str;
    }
}
