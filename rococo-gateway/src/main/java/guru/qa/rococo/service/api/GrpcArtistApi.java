package guru.qa.rococo.service.api;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.RococoArtistServiceBlockingStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
public class GrpcArtistApi {

    @GrpcClient("grpcArtistClient")
    private RococoArtistServiceBlockingStub rococoArtistServiceBlockingStub;

    public AllArtistsResponse getAllArtists(
            Integer page,
            Integer size,
            @Nullable String name
    ) {
        AllArtistsRequest request = name == null ? AllArtistsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build()
                : AllArtistsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .setName(name)
                .build();
        return rococoArtistServiceBlockingStub.getAllArtists(request);
    }

    public Artist getArtistById(String id) {
        return rococoArtistServiceBlockingStub.getArtistById(
                GetArtistRequest
                        .newBuilder()
                        .setId(id)
                        .build()
        );
    }

    public Artist addArtist(Artist artist) {
        return rococoArtistServiceBlockingStub.addArtist(artist);
    }

    public Artist updateArtist(Artist artist) {
        return rococoArtistServiceBlockingStub.updateArtist(artist);
    }
}
