package guru.qa.rococo.config;

public class DockerConfig implements Config {

    static final DockerConfig instance = new DockerConfig();

    @Override
    public String frontUrl() {
        return "http://client.rococo.dc";
    }

    @Override
    public String authUrl() {
        return "http://auth.rococo.dc:9000";
    }

    @Override
    public String dbUrl() {
        return "rococo-db";
    }

    @Override
    public String countryGrpcHost() {
        return "country.rococo.dc";
    }

    @Override
    public String museumGrpcHost() {
        return "museum.rococo.dc";
    }

    @Override
    public String artistGrpcHost() {
        return "artist.rococo.dc";
    }

    @Override
    public String paintingGrpcHost() {
        return "painting.rococo.dc";
    }
}
