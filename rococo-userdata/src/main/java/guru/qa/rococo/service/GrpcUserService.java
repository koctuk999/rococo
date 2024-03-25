package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.RococoUserProto;
import guru.qa.grpc.rococo.grpc.RococoUserServiceGrpc;
import guru.qa.grpc.rococo.grpc.User;
import guru.qa.grpc.rococo.grpc.UserRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GrpcUserService extends RococoUserServiceGrpc.RococoUserServiceImplBase {
    private final UserService userService;

    @Autowired
    public GrpcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(userService.getUser(request.getUsername()));
        responseObserver.onCompleted();
    }
}
