package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.model.ArtistEntityBuilder;
import guru.qa.rococo.model.KafkaUpdatedJson;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;
import java.util.UUID;

import static com.google.rpc.Code.NOT_FOUND;
import static com.google.rpc.Status.newBuilder;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.fromString;


@GrpcService
public class GrpcArtistService extends RococoArtistServiceGrpc.RococoArtistServiceImplBase {
    private static final Logger LOG = LoggerFactory.getLogger(GrpcArtistService.class);

    private final ArtistRepository artistRepository;

    private final KafkaTemplate kafkaTemplate;

    private final String topic;

    @Autowired
    public GrpcArtistService(
            ArtistRepository artistRepository,
            KafkaTemplate kafkaTemplate,
            @Value("${topic.name}") String topic) {
        this.artistRepository = artistRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void getAllArtists(AllArtistsRequest request, StreamObserver<AllArtistsResponse> responseObserver) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());
        Page<ArtistEntity> artistEntities = request.getName().isEmpty() ? artistRepository.findAll(pageRequest)
                : artistRepository.findAllByNameContainsIgnoreCase(request.getName(), pageRequest);
        responseObserver.onNext(
                AllArtistsResponse.newBuilder()
                        .addAllArtist(
                                artistEntities
                                        .stream()
                                        .map(artistEntity -> toGrpcArtist(artistEntity))
                                        .toList()
                        )
                        .setTotalCount(artistEntities.getTotalElements())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getArtistById(GetArtistRequest request, StreamObserver<Artist> responseObserver) {
        Optional<ArtistEntity> artistEntity = artistRepository.findById(fromString(request.getId()));
        if (artistEntity.isEmpty()) {
            com.google.rpc.Status status = newBuilder()
                    .setCode(NOT_FOUND.getNumber())
                    .setMessage("Artist not found")
                    .build();
            responseObserver.onError(toStatusRuntimeException(status));
        }
        responseObserver.onNext(
                toGrpcArtist(artistEntity.get())
        );
        responseObserver.onCompleted();
    }

    @Override
    public void addArtist(Artist request, StreamObserver<Artist> responseObserver) {
        ArtistEntity artistEntity = toArtistEntity(request);
        artistRepository.save(artistEntity);
        responseObserver.onNext(toGrpcArtist(artistEntity));
        responseObserver.onCompleted();
    }

    @Override
    public void updateArtist(Artist request, StreamObserver<Artist> responseObserver) {
        LOG.info("### Start update artist " + request.getId());
        Optional<ArtistEntity> artistById = artistRepository.findById(fromString(request.getId()));
        if (artistById.isEmpty()) {
            com.google.rpc.Status status = newBuilder()
                    .setCode(NOT_FOUND.getNumber())
                    .setMessage("Artist not found")
                    .build();
            responseObserver.onError(toStatusRuntimeException(status));
        } else {
            ArtistEntity artistEntity = artistById.get();
            artistEntity.setName(request.getName());
            artistEntity.setBiography(request.getBiography());
            artistEntity.setPhoto(request.getPhoto().getBytes());
            responseObserver.onNext(toGrpcArtist(artistRepository.save(artistEntity)));
            LOG.info("### Artist updated " + artistEntity.getId());

            KafkaUpdatedJson kafkaUpdatedJson = new KafkaUpdatedJson(
                    "artist",
                    artistEntity.getId()
            );
            kafkaTemplate.send(topic,kafkaUpdatedJson);
            LOG.info("### Kafka topic [updated] sent message %s###".formatted(kafkaUpdatedJson));
        }
        responseObserver.onCompleted();
    }

    private Artist toGrpcArtist(ArtistEntity artistEntity) {
        return Artist
                .newBuilder()
                .setId(artistEntity.getId().toString())
                .setName(artistEntity.getName())
                .setBiography(artistEntity.getBiography())
                .setPhoto(new String(artistEntity.getPhoto(), UTF_8))
                .build();
    }

    private ArtistEntity toArtistEntity(Artist artist) {
        return new ArtistEntityBuilder()
                .setId(artist.getId().isEmpty() ? null : UUID.fromString(artist.getId()))
                .setName(artist.getName())
                .setBiography(artist.getBiography())
                .setPhoto(artist.getPhoto().getBytes())
                .build();
    }
}
