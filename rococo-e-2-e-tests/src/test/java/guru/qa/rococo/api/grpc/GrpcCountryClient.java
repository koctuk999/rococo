package guru.qa.rococo.api.grpc;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.grpc.rococo.grpc.RococoCountryServiceGrpc.RococoCountryServiceBlockingStub;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.utils.GrpcLogInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

import javax.annotation.Nullable;

import static guru.qa.grpc.rococo.grpc.RococoCountryServiceGrpc.newBlockingStub;

public class GrpcCountryClient {
    private static final Config CFG = Config.getInstance();

    private static final Channel countryChannel = ManagedChannelBuilder.forAddress(CFG.countryGrpcHost(), CFG.countryGrpcPort())
            .intercept(new AllureGrpc(), new GrpcLogInterceptor())
            .usePlaintext()
            .build();

    private static final RococoCountryServiceBlockingStub rococoCountryServiceBlockingStub = newBlockingStub(countryChannel);

    public CountriesResponse getCountries(
            Integer size,
            @Nullable Integer page
    ) {
        CountriesRequest.Builder builder = CountriesRequest
                .newBuilder()
                .setSize(size);
        if (page != null) builder.setPage(page);
        return rococoCountryServiceBlockingStub
                .getCountries(builder.build());

    }

    @Nullable
    public Country getCountryByName(String name) {
        try {
            return rococoCountryServiceBlockingStub
                    .getCountryByName(
                            CountryByNameRequest
                                    .newBuilder()
                                    .setName(name)
                                    .build()
                    );
        } catch (Throwable e) {
            throw new IllegalStateException("Country %s not found".formatted(name));
        }
    }
}
