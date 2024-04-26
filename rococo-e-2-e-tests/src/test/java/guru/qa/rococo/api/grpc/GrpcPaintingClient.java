package guru.qa.rococo.api.grpc;


import guru.qa.grpc.rococo.grpc.*;
import guru.qa.grpc.rococo.grpc.RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub;
import guru.qa.rococo.api.grpc.channel.ChannelProvider;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.utils.GrpcLogInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.grpc.AllureGrpc;

import java.util.Optional;

import static org.apache.commons.lang3.tuple.Pair.of;

public class GrpcPaintingClient {

    private static final Config CFG = Config.getInstance();

    private static final Channel paintingChannel = ChannelProvider
            .INSTANCE
            .channel(of(CFG.paintingGrpcHost(), CFG.paintingGrpcPort()));

    private static final RococoPaintingServiceBlockingStub rococoPaintingServiceBlockingStub = RococoPaintingServiceGrpc.newBlockingStub(paintingChannel);

    private AllPaintingsResponse getAllPaintings(int size, int page) {
        return rococoPaintingServiceBlockingStub.getAllPaintings(
                AllPaintingsRequest
                        .newBuilder()
                        .setSize(size)
                        .setPage(page)
                        .build()
        );
    }

    @Step("Find painting {0} in list with pagination")
    public Optional<Painting> findPaintingInList(String paintingId) {
        int page = 0;
        while (true) {
            AllPaintingsResponse allPaintings = getAllPaintings(10, page);
            if (allPaintings.getPaintingList() == null) {
                return Optional.empty();
            }
            Optional<Painting> foundPainting = allPaintings.getPaintingList()
                    .stream()
                    .filter(painting -> painting.getId().equals(paintingId))
                    .findFirst();

            if (foundPainting.isPresent()) {
                return foundPainting;
            }
            page++;
        }
    }

    public Painting getPaintingById(String id) {
        return rococoPaintingServiceBlockingStub.getPaintingById(
                GetPaintingByIdRequest
                        .newBuilder()
                        .setId(id)
                        .build()
        );
    }

    public Painting addPainting(Painting painting) {
        return rococoPaintingServiceBlockingStub.addPainting(painting);
    }

    public Painting updatePainting(Painting painting) {
        return rococoPaintingServiceBlockingStub.updatePainting(painting);
    }
}
