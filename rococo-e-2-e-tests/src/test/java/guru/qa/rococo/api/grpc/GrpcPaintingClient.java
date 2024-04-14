package guru.qa.rococo.api.grpc;


import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.grpc.rococo.grpc.RococoPaintingServiceGrpc;
import guru.qa.grpc.rococo.grpc.RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.utils.GrpcLogInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

public class GrpcPaintingClient {

    private static final Config CFG = Config.getInstance();

    private static final Channel paintingChannel = ManagedChannelBuilder
            .forAddress(CFG.paintingGrpcHost(), CFG.paintingGrpcPort())
            .intercept(new AllureGrpc(), new GrpcLogInterceptor())
            .usePlaintext()
            .build();

    private static final RococoPaintingServiceBlockingStub rococoPaintingServiceBlockingStub = RococoPaintingServiceGrpc.newBlockingStub(paintingChannel);

    public Painting addPainting(Painting painting){
        return rococoPaintingServiceBlockingStub.addPainting(painting);
    }
}
