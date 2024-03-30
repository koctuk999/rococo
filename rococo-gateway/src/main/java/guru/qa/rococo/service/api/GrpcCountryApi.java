package guru.qa.rococo.service.api;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.grpc.rococo.grpc.RococoCountryServiceGrpc.RococoCountryServiceBlockingStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GrpcCountryApi {
    @GrpcClient("grpcCountryClient")
    private RococoCountryServiceBlockingStub rococoCountryServiceBlockingStub;

    public CountriesResponse getCountries(Integer page, Integer size) {
        return rococoCountryServiceBlockingStub.getCountries(
                CountriesRequest.newBuilder()
                        .setPage(page)
                        .setSize(size)
                        .build()
        );
    }
}
