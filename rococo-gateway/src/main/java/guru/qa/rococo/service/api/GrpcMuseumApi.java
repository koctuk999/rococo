package guru.qa.rococo.service.api;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.grpc.rococo.grpc.RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
public class GrpcMuseumApi {

    @GrpcClient("grpcMuseumClient")
    private RococoMuseumServiceBlockingStub rococoMuseumServiceBlockingStub;

    public AllMuseumsResponse getAllMuseum(Integer page, Integer size, @Nullable String title) {
        AllMuseumsRequest request = title == null ? AllMuseumsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build()
                : AllMuseumsRequest.newBuilder()
                .setTitle(title)
                .setPage(page)
                .setSize(size)
                .build();
        return rococoMuseumServiceBlockingStub.getAllMuseums(request);
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
