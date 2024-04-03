package guru.qa.rococo.service.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.service.ConsumerService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.HOURS;

@Service
public class GrpcArtistApi {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcArtistApi.class);


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

    public void refreshCache(UUID id) {
        if (artistCache.getIfPresent(id) != null){
            artistCache.refresh(id);
            LOG.info("### Refreshed artist-cache for ID:%s ###".formatted(id));
        }
    }
}
