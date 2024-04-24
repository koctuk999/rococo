package guru.qa.rococo.api.rest.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.api.rest.model.GetAllResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface MuseumGatewayApi {

    @GET("/api/museum")
    Call<GetAllResponse> getAllMuseums(@Query("size") String size);

    @POST("/api/museum")
    Call<JsonNode> addMuseum(@Body JsonNode museumJson, @Header("Authorization") String token);
}
