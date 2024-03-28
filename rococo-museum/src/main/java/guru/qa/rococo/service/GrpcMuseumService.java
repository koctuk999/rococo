package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.model.MuseumEntityBuilder;
import guru.qa.rococo.service.api.GrpcCountryApi;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import static com.google.rpc.Code.NOT_FOUND;
import static com.google.rpc.Status.newBuilder;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.fromString;

@GrpcService
public class GrpcMuseumService extends RococoMuseumServiceGrpc.RococoMuseumServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcMuseumService.class);

    private final MuseumRepository museumRepository;
    private final GrpcCountryApi grpcCountryApi;

    @Autowired
    public GrpcMuseumService(MuseumRepository museumRepository, GrpcCountryApi grpcCountryApi) {
        this.museumRepository = museumRepository;
        this.grpcCountryApi = grpcCountryApi;
    }

    @Override
    public void getAllMuseums(AllMuseumsRequest request, StreamObserver<AllMuseumsResponse> responseObserver) {
        Page<MuseumEntity> museumEntities = request.getTitle().isEmpty() ? museumRepository.findAll(PageRequest.of(request.getPage(), request.getSize()))
                : museumRepository.findAllByTitleContainsIgnoreCase(request.getTitle(), PageRequest.of(request.getPage(), request.getSize()));
        responseObserver.onNext(
                AllMuseumsResponse
                        .newBuilder()
                        .addAllMuseum(
                                museumEntities
                                        .stream()
                                        .map(museumEntity -> toGrpcMuseum(museumEntity))
                                        .toList()
                        )
                        .setTotalCount(museumEntities.getTotalElements())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getMuseumById(GetMuseumRequest request, StreamObserver<Museum> responseObserver) {
        Optional<MuseumEntity> museumEntity = museumRepository.findById(fromString(request.getId()));
        if (museumEntity.isEmpty()) {
            com.google.rpc.Status status = newBuilder()
                    .setCode(NOT_FOUND.getNumber())
                    .setMessage("Museum not found")
                    .build();
            responseObserver.onError(toStatusRuntimeException(status));
        }
        responseObserver.onNext(
                toGrpcMuseum(museumEntity.get())
        );
        responseObserver.onCompleted();
    }

    @Override
    public void addMuseum(Museum request, StreamObserver<Museum> responseObserver) {
        MuseumEntity museumEntity = toMuseumEntity(request);
        museumRepository.save(museumEntity);
        responseObserver.onNext(toGrpcMuseum(museumEntity));
        responseObserver.onCompleted();
    }

    @Override
    public void updateMuseum(Museum request, StreamObserver<Museum> responseObserver) {
        LOG.info("### Start update museum " + request.getId());
        Optional<MuseumEntity> museumById = museumRepository.findById(fromString(request.getId()));
        if (museumById.isEmpty()) {
            com.google.rpc.Status status = newBuilder()
                    .setCode(NOT_FOUND.getNumber())
                    .setMessage("Museum not found")
                    .build();
            responseObserver.onError(toStatusRuntimeException(status));
        } else {
            MuseumEntity museumEntity = museumById.get();
            museumEntity.setTitle(request.getTitle());
            museumEntity.setDescription(request.getDescription());
            museumEntity.setPhoto(request.getPhoto().getBytes());
            museumEntity.setCity(request.getGeo().getCity());
            museumEntity.setCountryId(fromString(request.getGeo().getCountry().getId()));
            responseObserver.onNext(toGrpcMuseum(museumRepository.save(museumEntity)));
            LOG.info("### Museum updated " + museumEntity.getId());
        }
        responseObserver.onCompleted();
    }

    private Museum toGrpcMuseum(MuseumEntity museumEntity) {
        String photo = new String(museumEntity.getPhoto(), UTF_8);
        Country country = grpcCountryApi.getCountryCache(museumEntity.getCountryId());
        Geo geo = Geo.newBuilder()
                .setCity(museumEntity.getCity())
                .setCountry(country)
                .build();
        return Museum.newBuilder()
                .setId(museumEntity.getId().toString())
                .setTitle(museumEntity.getTitle())
                .setDescription(museumEntity.getDescription())
                .setPhoto(photo)
                .setGeo(geo).build();
    }

    private MuseumEntity toMuseumEntity(Museum museum) {
        return new MuseumEntityBuilder()
                .setTitle(museum.getTitle())
                .setDescription(museum.getDescription())
                .setPhoto(museum.getPhoto().getBytes())
                .setCity(museum.getGeo().getCity())
                .setCountryId(UUID.fromString(museum.getGeo().getCountry().getId()))
                .build();
    }
}
