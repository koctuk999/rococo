package guru.qa.rococo.service.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import guru.qa.grpc.rococo.grpc.Country;
import guru.qa.grpc.rococo.grpc.GetMuseumRequest;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc;
import guru.qa.rococo.service.ConsumerService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.HOURS;

@Service
public class GrpcMuseumApi {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcMuseumApi.class);


    @GrpcClient("grpcMuseumClient")
    private RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub rococoMuseumServiceBlockingStub;

    private LoadingCache<UUID, Museum> museumCache = Caffeine.newBuilder()
            .expireAfterWrite(1, HOURS)
            .build(id -> getMuseumById(id));

    private Museum getMuseumById(UUID id) {
        return rococoMuseumServiceBlockingStub.getMuseumById(
                GetMuseumRequest
                        .newBuilder()
                        .setId(id.toString())
                        .build()
        );
    }

    public Museum getMuseumCache(UUID id) {
        return museumCache.get(id);
    }

    public void refreshCache(UUID id) {
        if (museumCache.getIfPresent(id) != null){
            museumCache.refresh(id);
            LOG.info("### Refreshed museum-cache for ID:%s ###".formatted(id));
        }
    }
}
