package guru.qa.rococo.api.grpc;

import guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.RococoArtistServiceBlockingStub;
import guru.qa.rococo.config.Config;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

import static guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.newBlockingStub;

public class GrpcArtistClient {

    private static final Config CFG = Config.getInstance();

    private static final Channel artistChannel = ManagedChannelBuilder
            .forAddress(CFG.artistGrpcHost(), CFG.artistGrpcPort())
            .intercept(new AllureGrpc())
            .usePlaintext()
            .build();

    private static final RococoArtistServiceBlockingStub rococoArtistServiceBlockingStub = newBlockingStub(artistChannel);
}
