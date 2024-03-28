package guru.qa.rococo.model;

import lombok.Getter;
import lombok.Setter;

public record SessionJson(
        String username,
        String issuedAt,
        String expiresAt
) {

}
