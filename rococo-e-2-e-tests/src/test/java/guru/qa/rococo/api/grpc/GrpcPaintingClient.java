package guru.qa.rococo.api.grpc;


import guru.qa.grpc.rococo.grpc.Painting;
import guru.qa.grpc.rococo.grpc.RococoPaintingServiceGrpc;
import guru.qa.grpc.rococo.grpc.RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub;
import guru.qa.rococo.api.grpc.channel.ChannelProvider;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.utils.GrpcLogInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

import static org.apache.commons.lang3.tuple.Pair.of;

public class GrpcPaintingClient {

    private static final Config CFG = Config.getInstance();

    private static final Channel paintingChannel = ChannelProvider
            .INSTANCE
            .channel(of(CFG.paintingGrpcHost(), CFG.paintingGrpcPort()));

    private static final RococoPaintingServiceBlockingStub rococoPaintingServiceBlockingStub = RococoPaintingServiceGrpc.newBlockingStub(paintingChannel);

    public Painting addPainting(Painting painting) {
        return rococoPaintingServiceBlockingStub.addPainting(painting);
    }
}
