package guru.qa.rococo.service.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import guru.qa.grpc.rococo.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.HOURS;

@Service
public class GrpcArtistApi {

    @GrpcClient("grpcArtistClient")
    private RococoArtistServiceGrpc.RococoArtistServiceBlockingStub rococoArtistServiceBlockingStub;

    private LoadingCache<UUID, Artist> artistCache = Caffeine.newBuilder()
            .expireAfterWrite(1, HOURS)
            .build(id -> getArtistById(id));

    private Artist getArtistById(UUID id) {
        return rococoArtistServiceBlockingStub.getArtistById(
                GetArtistRequest
                        .newBuilder()
                        .setId(id.toString())
                        .build()
        );
    }

    public Artist getArtistCache(UUID id) {
        return artistCache.get(id);
    }
}
