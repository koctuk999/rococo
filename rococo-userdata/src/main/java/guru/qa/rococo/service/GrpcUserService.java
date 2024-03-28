package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.RococoUserProto;
import guru.qa.grpc.rococo.grpc.RococoUserServiceGrpc;
import guru.qa.grpc.rococo.grpc.User;
import guru.qa.grpc.rococo.grpc.UserRequest;
import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.google.rpc.Code.NOT_FOUND;
import static com.google.rpc.Status.newBuilder;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@GrpcService
public class GrpcUserService extends RococoUserServiceGrpc.RococoUserServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserService.class);
    private final UserRepository userRepository;

    @Autowired
    public GrpcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<User> responseObserver) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(request.getUsername());
        if (userEntity.isEmpty()) {
            com.google.rpc.Status status = newBuilder()
                    .setCode(NOT_FOUND.getNumber())
                    .setMessage("User not found")
                    .build();
            responseObserver.onError(toStatusRuntimeException(status));
        } else {
            responseObserver.onNext(userEntity.get().toGrpc());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(User request, StreamObserver<User> responseObserver) {
        Optional<UserEntity> userById = userRepository.findByUsername(request.getUsername());
        if (userById.isEmpty()) {
            com.google.rpc.Status status = newBuilder()
                    .setCode(NOT_FOUND.getNumber())
                    .setMessage("User not found")
                    .build();
            responseObserver.onError(toStatusRuntimeException(status));
        } else {
            UserEntity userEntity = userById.get();
            userEntity.setFirstname(request.getFirstname());
            userEntity.setLastname(request.getLastname());
            userEntity.setAvatar(request.getAvatar().getBytes());
            userRepository.save(userEntity);
            responseObserver.onNext(userEntity.toGrpc());
            LOG.info("### User updated " + userEntity.getId());
        }
        responseObserver.onCompleted();
    }
}
