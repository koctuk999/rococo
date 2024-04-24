package guru.qa.rococo.config;

public interface Config {

    static Config getInstance() {
        return "docker".equals(System.getProperty("test.env"))
                ? DockerConfig.instance
                : LocalConfig.instance;
    }

    String frontUrl();

    String authUrl();

    String gatewayUrl();

    String dbUrl();

    String countryGrpcHost();

    String museumGrpcHost();

    String artistGrpcHost();

    String paintingGrpcHost();

    default Integer dbPort() {
        return 5432;
    }

    default Integer countryGrpcPort() {
        return 9093;
    }

    default Integer museumGrpcPort() {
        return 9094;
    }

    default Integer artistGrpcPort() {
        return 9095;
    }

    default Integer paintingGrpcPort() {
        return 9096;
    }

    default String dbUsername() {
        return "postgres";
    }

    default String dbSecret() {
        return "secret";
    }
}
