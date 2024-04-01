package guru.qa.rococo.service.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import guru.qa.grpc.rococo.grpc.Country;
import guru.qa.grpc.rococo.grpc.GetMuseumRequest;
import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.HOURS;

@Service
public class GrpcMuseumApi {

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
}
