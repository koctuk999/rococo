package guru.qa.rococo.db.model;

import jakarta.persistence.Column;

import java.util.UUID;

public record TestUser(
        String username,
        String password,
        String firstname,
        String lastname,
        byte[] avatar,
        UUID authId,
        UUID dataId
) {
}
