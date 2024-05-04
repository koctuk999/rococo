package guru.qa.rococo.db.repository.museum;

import guru.qa.rococo.db.model.MuseumEntity;

import java.util.Optional;
import java.util.UUID;

public interface MuseumRepository {

    MuseumEntity findMuseumByTitle(String title);
    MuseumEntity findMuseumById(UUID id);
}
