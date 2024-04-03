package guru.qa.rococo.model;

import java.util.UUID;

public record KafkaUpdatedJson(
        String updatedEntity,
        UUID entityId
) {
}
