package guru.qa.rococo.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.grpc.rococo.grpc.AllPaintingsResponse;
import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.rococo.service.api.GrpcPaintingApi;
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
public class PaintingService {
    private final GrpcPaintingApi grpcPaintingApi;

    @Autowired
    public PaintingService(GrpcPaintingApi grpcPaintingApi) {
        this.grpcPaintingApi = grpcPaintingApi;
    }

    public Page<JsonNode> getAllPainting(Pageable pageable, @Nullable String title) {
        AllPaintingsResponse allPaintingsResponse = grpcPaintingApi.getAllPainting(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                title
        );
        List<JsonNode> paintingList = allPaintingsResponse.getPaintingList()
                .stream()
                .map(painting -> convertGrpcToJson(painting))
                .toList();
        return new PageImpl<>(
                paintingList,
                pageable,
                allPaintingsResponse.getTotalCount()
        );
    }

    public Page<JsonNode> getPaintingByArtist(Pageable pageable, String artist) {
        AllPaintingsResponse allPaintingsResponse = grpcPaintingApi.getPaintingByArtist(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                artist
        );
        List<JsonNode> paintingList = allPaintingsResponse.getPaintingList()
                .stream()
                .map(painting -> convertGrpcToJson(painting))
                .toList();
        return new PageImpl<>(
                paintingList,
                pageable,
                allPaintingsResponse.getTotalCount()
        );
    }

    public JsonNode getPaintingById(String id) {
        return convertGrpcToJson(grpcPaintingApi.getPaintingById(id));
    }

    public JsonNode addPainting(JsonNode paintingJson) {
        Painting painting = grpcPaintingApi.addPainting(convertJsonToGrpc(paintingJson, Painting.class));
        JsonNode addedPainting = convertGrpcToJson(painting);
        return addedPainting;
    }

    public JsonNode updatePainting(JsonNode paintingJson) {
        Painting painting = grpcPaintingApi.updatePainting(convertJsonToGrpc(paintingJson, Painting.class));
        JsonNode updatedPainting = convertGrpcToJson(painting);
        return updatedPainting;
    }
}
