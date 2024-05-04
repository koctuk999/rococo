package guru.qa.rococo.db.repository.artist;

import guru.qa.rococo.db.model.ArtistEntity;
import java.util.UUID;

public interface ArtistRepository {
    ArtistEntity findArtistByName(String name);
    ArtistEntity findArtistById(UUID id);
}
