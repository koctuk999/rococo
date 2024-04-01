package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.data.PaintingRepository;
import guru.qa.rococo.service.api.GrpcArtistApi;
import guru.qa.rococo.service.api.GrpcMuseumApi;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static com.google.rpc.Code.NOT_FOUND;
import static com.google.rpc.Status.newBuilder;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.fromString;

@GrpcService
public class GrpcPaintingService extends RococoPaintingServiceGrpc.RococoPaintingServiceImplBase {
    private static final Logger LOG = LoggerFactory.getLogger(GrpcPaintingService.class);
    private final PaintingRepository paintingRepository;
    private final GrpcArtistApi grpcArtistApi;
    private final GrpcMuseumApi grpcMuseumApi;

    @Autowired
    public GrpcPaintingService(PaintingRepository paintingRepository, GrpcArtistApi grpcArtistApi, GrpcMuseumApi grpcMuseumApi) {
        this.paintingRepository = paintingRepository;
        this.grpcArtistApi = grpcArtistApi;
        this.grpcMuseumApi = grpcMuseumApi;
    }

    @Override
    public void getAllPaintings(AllPaintingsRequest request, StreamObserver<AllPaintingsResponse> responseObserver) {
        Page<PaintingEntity> paintingEntities = request.getTitle().isEmpty() ? paintingRepository.findAll(PageRequest.of(request.getPage(), request.getSize()))
                : paintingRepository.findAllByTitleContainsIgnoreCase(request.getTitle(), PageRequest.of(request.getPage(), request.getSize()));
        responseObserver.onNext(
                AllPaintingsResponse
                        .newBuilder()
                        .addAllPainting(
                                paintingEntities
                                        .stream()
                                        .map(paintingEntity -> toGrpcPainting(paintingEntity))
                                        .toList()
                        )
                        .setTotalCount(paintingEntities.getTotalElements())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getPaintingByArtist(GetPaintingByArtistRequest request, StreamObserver<AllPaintingsResponse> responseObserver) {
        Page<PaintingEntity> paintingEntities = paintingRepository.findAllByArtistId(
                UUID.fromString(request.getArtist()),
                PageRequest.of(request.getPage(), request.getSize()));
        responseObserver.onNext(
                AllPaintingsResponse
                        .newBuilder()
                        .addAllPainting(
                                paintingEntities
                                        .stream()
                                        .map(paintingEntity -> toGrpcPainting(paintingEntity))
                                        .toList()
                        )
                        .setTotalCount(paintingEntities.getTotalElements())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getPaintingById(GetPaintingByIdRequest request, StreamObserver<Painting> responseObserver) {
        Optional<PaintingEntity> paintingEntity = paintingRepository.findById(fromString(request.getId()));
        if (paintingEntity.isEmpty()) {
            com.google.rpc.Status status = newBuilder()
                    .setCode(NOT_FOUND.getNumber())
                    .setMessage("Painting not found")
                    .build();
            responseObserver.onError(toStatusRuntimeException(status));
        } else {
            responseObserver.onNext(
                    toGrpcPainting(paintingEntity.get())
            );
        }
        responseObserver.onCompleted();
    }

    @Override
    public void addPainting(Painting request, StreamObserver<Painting> responseObserver) {
        PaintingEntity paintingEntity = toPaintingEntity(request);
        paintingRepository.save(paintingEntity);
        responseObserver.onNext(toGrpcPainting(paintingEntity));
        responseObserver.onCompleted();
    }

    @Override
    public void updatePainting(Painting request, StreamObserver<Painting> responseObserver) {
        LOG.info("### Start update painting " + request.getId());
        Optional<PaintingEntity> paintingById = paintingRepository.findById(fromString(request.getId()));
        if (paintingById.isEmpty()) {
            com.google.rpc.Status status = newBuilder()
                    .setCode(NOT_FOUND.getNumber())
                    .setMessage("Painting not found")
                    .build();
            responseObserver.onError(toStatusRuntimeException(status));
        } else {
            PaintingEntity paintingEntity = paintingById.get();
            paintingEntity.setTitle(request.getTitle());
            paintingEntity.setDescription(request.getDescription());
            paintingEntity.setContent(request.getContent().getBytes());
            paintingEntity.setArtistId(fromString(request.getArtist().getId()));
            paintingEntity.setMuseumId(fromString(request.getMuseum().getId()));
            responseObserver.onNext(toGrpcPainting(paintingRepository.save(paintingEntity)));
            LOG.info("### Painting updated " + paintingEntity.getId());
        }
        responseObserver.onCompleted();
    }

    private Painting toGrpcPainting(PaintingEntity paintingEntity) {
        String content = new String(paintingEntity.getContent(), UTF_8);
        Artist artist = grpcArtistApi.getArtistCache(paintingEntity.getArtistId());
        Museum museum = grpcMuseumApi.getMuseumCache(paintingEntity.getMuseumId());
        return Painting
                .newBuilder()
                .setId(paintingEntity.getId().toString())
                .setTitle(paintingEntity.getTitle())
                .setDescription(paintingEntity.getDescription())
                .setContent(content)
                .setArtist(artist)
                .setMuseum(museum)
                .build();
    }

    private PaintingEntity toPaintingEntity(Painting painting) {
        return PaintingEntity.builder()
                .id(painting.getId().isEmpty() ? null : fromString(painting.getId()))
                .title(painting.getTitle())
                .description(painting.getDescription())
                .content(painting.getContent().getBytes())
                .artistId(fromString(painting.getArtist().getId()))
                .museumId(fromString(painting.getMuseum().getId()))
                .build();
    }
}
