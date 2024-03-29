package guru.qa.rococo.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.grpc.rococo.grpc.AllArtistsResponse;
import guru.qa.grpc.rococo.grpc.AllMuseumsResponse;
import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.rococo.service.api.GrpcArtistApi;
import guru.qa.rococo.utils.Hellpers;
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
public class ArtistService {
    private final GrpcArtistApi grpcArtistApi;

    @Autowired
    public ArtistService(GrpcArtistApi grpcArtistApi) {
        this.grpcArtistApi = grpcArtistApi;
    }

    public Page<JsonNode> getAllArtists(Pageable pageable, @Nullable String name) {
        AllArtistsResponse allArtistsResponse = grpcArtistApi.getAllArtists(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                name
        );
        List<JsonNode> artistsList = allArtistsResponse.getArtistList()
                .stream()
                .map(artist -> convertGrpcToJson(artist))
                .toList();
        return new PageImpl<>(
                artistsList,
                pageable,
                allArtistsResponse.getTotalCount()
        );
    }

    public JsonNode getArtistById(String id) {
        return convertGrpcToJson(grpcArtistApi.getArtistById(id));
    }

    public JsonNode addArtist(JsonNode artistJson) {
        Artist artist = convertJsonToGrpc(artistJson, Artist.class);
        JsonNode addedArtist = convertGrpcToJson(grpcArtistApi.addArtist(artist));
        return addedArtist;
    }

    public JsonNode updateArtist(JsonNode artistJson) {
        Artist artist = convertJsonToGrpc(artistJson, Artist.class);
        JsonNode updatedArtist = convertGrpcToJson(grpcArtistApi.updateArtist(artist));
        return updatedArtist;
    }
}
