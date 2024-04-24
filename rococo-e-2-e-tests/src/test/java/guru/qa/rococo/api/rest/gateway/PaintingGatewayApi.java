package guru.qa.rococo.api.rest.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.api.rest.model.GetAllResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface PaintingGatewayApi {

    @GET("/api/painting")
    Call<GetAllResponse> getAllPaintings(@Query("size") String size);

    @POST("/api/painting")
    Call<JsonNode> addPainting(@Body JsonNode paintingJson, @Header("Authorization") String token);
}
