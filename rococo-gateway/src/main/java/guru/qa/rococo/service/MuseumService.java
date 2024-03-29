package guru.qa.rococo.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.grpc.rococo.grpc.AllMuseumsResponse;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.rococo.service.api.GrpcMuseumApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

import static guru.qa.rococo.utils.Hellpers.convertGrpcToJson;
import static guru.qa.rococo.utils.Hellpers.convertJsonToGrpc;

@Service
public class MuseumService {
    private final GrpcMuseumApi grpcMuseumApi;

    @Autowired
    public MuseumService(GrpcMuseumApi grpcMuseumApi) {
        this.grpcMuseumApi = grpcMuseumApi;
    }

    public Page<JsonNode> getAllMuseum(
            Pageable pageable,
            @Nullable String title
    ) {
        AllMuseumsResponse allMuseumsResponse = grpcMuseumApi.getAllMuseum(pageable.getPageNumber(), pageable.getPageSize(), title);
        List<JsonNode> museumsList = allMuseumsResponse
                .getMuseumList()
                .stream()
                .map(museum -> convertGrpcToJson(museum))
                .toList();
        return new PageImpl<>(
                museumsList,
                pageable,
                allMuseumsResponse.getTotalCount()
        );
    }

    public JsonNode getMuseumById(String id) {
        return convertGrpcToJson(grpcMuseumApi.getMuseumById(id));
    }

    public JsonNode addMuseum(JsonNode museumJson) {
        Museum museum = convertJsonToGrpc(museumJson, Museum.class);
        JsonNode addedMuseum = convertGrpcToJson(grpcMuseumApi.addMuseum(museum));
        return addedMuseum;
    }

    public JsonNode updateMuseum(JsonNode museumJson) {
        Museum museum = convertJsonToGrpc(museumJson, Museum.class);
        JsonNode updatedMuseum = convertGrpcToJson(grpcMuseumApi.updateMuseum(museum));
        return updatedMuseum;
    }
}
