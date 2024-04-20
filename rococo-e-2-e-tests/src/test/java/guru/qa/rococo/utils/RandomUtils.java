package guru.qa.rococo.utils;

import com.github.javafaker.Faker;


public class RandomUtils {

    private static final Faker faker = new Faker();

    public static String genRandomUsername() {
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        return faker.bothify("????####");
    }

    public static String genRandomName() {
        return faker.name().firstName();
    }

    public static String genRandomLastName() {
        return faker.name().lastName();
    }

    public static String genRandomDescription(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    public static String genRandomTitle() {return faker.company().name();}

    public static String genRandomCity() {return faker.address().cityName();}
}
