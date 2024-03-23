package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KafkaUserJson(
        @JsonProperty("username")
        String username
) {
}
