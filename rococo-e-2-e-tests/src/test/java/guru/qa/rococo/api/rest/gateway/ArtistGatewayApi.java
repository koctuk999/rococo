package guru.qa.rococo.api.rest.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.api.rest.model.GetAllResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface ArtistGatewayApi {

    @GET("/api/artist")
    Call<GetAllResponse> getAllArtists(@Query("size") String size);

    @POST("/api/artist")
    Call<JsonNode> addArtist(@Body JsonNode artist, @Header("Authorization") String token);
}
