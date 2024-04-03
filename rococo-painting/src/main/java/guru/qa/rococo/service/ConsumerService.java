package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.GetArtistRequest;
import guru.qa.rococo.model.KafkaUpdatedJson;
import guru.qa.rococo.service.api.GrpcArtistApi;
import guru.qa.rococo.service.api.GrpcMuseumApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerService.class);

    private final GrpcMuseumApi grpcMuseumApi;
    private final GrpcArtistApi grpcArtistApi;

    @Autowired
    public ConsumerService(GrpcMuseumApi grpcMuseumApi, GrpcArtistApi grpcArtistApi) {
        this.grpcMuseumApi = grpcMuseumApi;
        this.grpcArtistApi = grpcArtistApi;
    }

    @KafkaListener(topics = "updated", groupId = "painting")
    public void kafkaListener(@Payload KafkaUpdatedJson updated) {
        LOG.info("### Kafka topic [updated] received message from %s ###".formatted(updated.updatedEntity()));
        switch (updated.updatedEntity()) {
            case "museum":
                grpcMuseumApi.refreshCache(updated.entityId());
                break;
            case "artist":
                grpcArtistApi.refreshCache(updated.entityId());
                break;
            default: LOG.info("###Received unknown entity ###");
        }
    }
}
