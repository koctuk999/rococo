package guru.qa.rococo.db.repository.artist;

import guru.qa.rococo.db.model.ArtistEntity;
import guru.qa.rococo.db.model.PaintingEntity;

public interface ArtistRepository {
    ArtistEntity findArtistByName(String name);

}
