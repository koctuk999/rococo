package guru.qa.rococo.service.api;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.grpc.rococo.grpc.RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
public class GrpcPaintingApi {

    @GrpcClient("grpcPaintingClient")
    private RococoPaintingServiceBlockingStub rococoPaintingServiceBlockingStub;

    public AllPaintingsResponse getAllPainting(Integer page, Integer size, @Nullable String title) {
        AllPaintingsRequest request = title == null ? AllPaintingsRequest
                .newBuilder()
                .setPage(page)
                .setSize(size)
                .build()
                : AllPaintingsRequest
                .newBuilder()
                .setTitle(title)
                .setSize(size)
                .setPage(page)
                .build();
        return rococoPaintingServiceBlockingStub.getAllPaintings(request);
    }

    public AllPaintingsResponse getPaintingByArtist(Integer page, Integer size, String artist) {
        return rococoPaintingServiceBlockingStub.getPaintingByArtist(
                GetPaintingByArtistRequest
                        .newBuilder()
                        .setPage(page)
                        .setSize(size)
                        .setArtist(artist)
                        .build()
        );
    }

    public Painting getPaintingById(String id) {
        return rococoPaintingServiceBlockingStub.getPaintingById(
                GetPaintingByIdRequest
                        .newBuilder()
                        .setId(id)
                        .build()
        );
    }

    public Painting addPainting(Painting painting) {
        return rococoPaintingServiceBlockingStub.addPainting(painting);
    }

    public Painting updatePainting(Painting painting) {
        return rococoPaintingServiceBlockingStub.updatePainting(painting);
    }
}
