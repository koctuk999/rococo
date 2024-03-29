package guru.qa.rococo.service.api;

import guru.qa.grpc.rococo.grpc.RococoUserServiceGrpc;
import guru.qa.grpc.rococo.grpc.RococoUserServiceGrpc.RococoUserServiceBlockingStub;
import guru.qa.grpc.rococo.grpc.User;
import guru.qa.grpc.rococo.grpc.UserRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GrpcUserApi {
    @GrpcClient("grpcUserClient")
    private RococoUserServiceBlockingStub rococoUserServiceBlockingStub;

    public User getUser(String username) {
        return rococoUserServiceBlockingStub.getUser(
                UserRequest.newBuilder()
                        .setUsername(username)
                        .build()
        );
    }

    public User updateUser(User user) {
        return rococoUserServiceBlockingStub.updateUser(user);
    }

}
