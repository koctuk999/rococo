package guru.qa.rococo.api.grpc;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub;
import guru.qa.rococo.api.grpc.channel.ChannelProvider;
import guru.qa.rococo.config.Config;
import io.grpc.Channel;
import io.qameta.allure.Step;

import java.util.Optional;

import static guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc.newBlockingStub;
import static org.apache.commons.lang3.tuple.Pair.of;

public class GrpcMuseumClient {

    private final Config CFG = Config.getInstance();

    private final Channel museumChannel = ChannelProvider
            .INSTANCE
            .channel(of(CFG.museumGrpcHost(), CFG.museumGrpcPort()));

    private final RococoMuseumServiceBlockingStub rococoMuseumServiceBlockingStub = newBlockingStub(museumChannel);

    private AllMuseumsResponse getAllMuseums(int size, int page) {
        return rococoMuseumServiceBlockingStub.getAllMuseums(
                AllMuseumsRequest
                        .newBuilder()
                        .setSize(size)
                        .setPage(page)
                        .build()
        );
    }

    @Step("Find museum {0} in list with pagination")
    public Optional<Museum> findMuseumInList(String museumId) {
        int page = 0;
        while (true) {
            AllMuseumsResponse allMuseums = getAllMuseums(10, page);
            if (allMuseums.getMuseumList() == null) {
                return Optional.empty();
            }

            Optional<Museum> foundMuseum = allMuseums.getMuseumList()
                    .stream()
                    .filter(museum -> museum.getId().equals(museumId))
                    .findFirst();

            if (foundMuseum.isPresent()) {
                return foundMuseum;
            }
            page++;
        }
    }

    public Museum getMuseumById(String id) {
        return rococoMuseumServiceBlockingStub.getMuseumById(
                GetMuseumRequest
                        .newBuilder()
                        .setId(id)
                        .build()
        );
    }

    public Museum addMuseum(Museum museum) {
        return rococoMuseumServiceBlockingStub.addMuseum(museum);
    }

    public Museum updateMuseum(Museum museum) {
        return rococoMuseumServiceBlockingStub.updateMuseum(museum);
    }
}
