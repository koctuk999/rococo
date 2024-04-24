package guru.qa.rococo.config;

public class LocalConfig implements Config {

    static final LocalConfig instance = new LocalConfig();

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3000";
    }

    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:8080";
    }

    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000";
    }

    @Override
    public String dbUrl() {
        return "localhost";
    }

    @Override
    public String countryGrpcHost() {
        return "localhost";
    }

    @Override
    public String museumGrpcHost() {
        return "localhost";
    }

    @Override
    public String artistGrpcHost() {
        return "localhost";
    }

    @Override
    public String paintingGrpcHost() {
        return "localhost";
    }
}
