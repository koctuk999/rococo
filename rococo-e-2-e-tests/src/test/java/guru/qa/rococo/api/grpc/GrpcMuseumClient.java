package guru.qa.rococo.api.grpc;

import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc;
import guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.utils.GrpcLogInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

import static guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc.newBlockingStub;

public class GrpcMuseumClient {

    private final Config CFG = Config.getInstance();

    private final Channel museumChannel = ManagedChannelBuilder.forAddress(CFG.museumGrpcHost(), CFG.museumGrpcPort())
            .intercept(new AllureGrpc(), new GrpcLogInterceptor())
            .usePlaintext()
            .build();

    private final RococoMuseumServiceBlockingStub rococoMuseumServiceBlockingStub = newBlockingStub(museumChannel);

    public Museum addMuseum(Museum museum) {
        return rococoMuseumServiceBlockingStub.addMuseum(museum);
    }
}
