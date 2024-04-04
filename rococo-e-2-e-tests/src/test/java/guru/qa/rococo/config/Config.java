package guru.qa.rococo.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.instance;
    }

    String frontUrl();

    String dbUrl();

    default Integer dbPort() {
        return 5432;
    }

    default String dbUsername() {
        return "postgres";
    }

    default String dbSecret() {
        return "secret";
    }
}
