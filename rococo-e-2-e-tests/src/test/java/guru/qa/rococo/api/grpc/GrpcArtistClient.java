package guru.qa.rococo.api.grpc;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.RococoArtistServiceBlockingStub;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.utils.GrpcLogInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

import static guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.newBlockingStub;

public class GrpcArtistClient {

    private static final Config CFG = Config.getInstance();

    private static final Channel artistChannel = ManagedChannelBuilder
            .forAddress(CFG.artistGrpcHost(), CFG.artistGrpcPort())
            .intercept(new AllureGrpc(), new GrpcLogInterceptor())
            .usePlaintext()
            .build();

    private static final RococoArtistServiceBlockingStub rococoArtistServiceBlockingStub = newBlockingStub(artistChannel);

    public Artist addArtist(Artist artist) {
        return rococoArtistServiceBlockingStub.addArtist(artist);
    }
}
