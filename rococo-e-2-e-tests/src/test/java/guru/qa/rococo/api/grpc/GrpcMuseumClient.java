package guru.qa.rococo.api.grpc;

import guru.qa.grpc.rococo.grpc.Museum;
import guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc;
import guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub;
import guru.qa.rococo.api.grpc.channel.ChannelProvider;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.utils.GrpcLogInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

import static guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc.newBlockingStub;
import static org.apache.commons.lang3.tuple.Pair.of;

public class GrpcMuseumClient {

    private final Config CFG = Config.getInstance();

    private final Channel museumChannel = ChannelProvider
            .INSTANCE
            .channel(of(CFG.museumGrpcHost(),CFG.museumGrpcPort()));

    private final RococoMuseumServiceBlockingStub rococoMuseumServiceBlockingStub = newBlockingStub(museumChannel);

    public Museum addMuseum(Museum museum) {
        return rococoMuseumServiceBlockingStub.addMuseum(museum);
    }
}
