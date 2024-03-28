package guru.qa.rococo.service.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import guru.qa.grpc.rococo.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.HOURS;

@Service
public class GrpcCountryApi {
    private static final Logger LOG = LoggerFactory.getLogger(GrpcCountryApi.class);

    @GrpcClient("grpcCountryClient")
    private RococoCountryServiceGrpc.RococoCountryServiceBlockingStub rococoCountryServiceBlockingStub;

    private LoadingCache<UUID, Country> countryCache = Caffeine.newBuilder()
            .expireAfterWrite(1, HOURS)
            .build(id -> getCountryById(id));

    private Country getCountryById(UUID id) {
        return rococoCountryServiceBlockingStub.getCountryById(
                CountryByIdRequest
                        .newBuilder()
                        .setId(id.toString())
                        .build()
        );
    }

    public Country getCountryCache(UUID id) {
        return countryCache.get(id);
    }
}
