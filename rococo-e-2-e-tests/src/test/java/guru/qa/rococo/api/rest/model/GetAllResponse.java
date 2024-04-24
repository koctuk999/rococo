package guru.qa.rococo.api.rest.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public record GetAllResponse(
        List<JsonNode> content,
        JsonNode pageable,
        int totalPages,
        int totalElements,
        boolean last,
        int size,
        int number,
        JsonNode sort,
        int numberOfElements,
        boolean first,
        boolean empty
) {
}
