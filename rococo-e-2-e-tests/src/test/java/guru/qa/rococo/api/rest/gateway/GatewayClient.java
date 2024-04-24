package guru.qa.rococo.api.rest.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.api.rest.RestClient;
import guru.qa.rococo.config.Config;
import io.qameta.allure.Step;

import java.io.IOException;


public class GatewayClient extends RestClient {
    private static final Config CFG = Config.getInstance();
    private final ArtistGatewayApi artistGatewayApi;
    private final MuseumGatewayApi museumGatewayApi;
    private final PaintingGatewayApi paintingGatewayApi;

    public GatewayClient() {
        super(CFG.gatewayUrl());
        this.artistGatewayApi = retrofit.create(ArtistGatewayApi.class);
        this.museumGatewayApi = retrofit.create(MuseumGatewayApi.class);
        this.paintingGatewayApi = retrofit.create(PaintingGatewayApi.class);
    }

    @Step("[http] POST/api/artist - send add artist request")
    public JsonNode addArtist(JsonNode artistJson, String token) {
        try {
            return artistGatewayApi.addArtist(artistJson, token)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("[http] POST/api/museum - send add museum request")
    public JsonNode addMuseum(JsonNode museumJson, String token)  {
        try {
            return museumGatewayApi.addMuseum(museumJson, token)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("[http] POST/api/painting - send add painting request")
    public JsonNode addPainting(JsonNode paintingJson, String token) {
        try {
            return paintingGatewayApi.addPainting(paintingJson, token)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
