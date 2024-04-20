package guru.qa.rococo.db.repository.museum;

import guru.qa.rococo.db.model.MuseumEntity;

import java.util.Optional;

public interface MuseumRepository {

    MuseumEntity findMuseumByTitle(String title);
}
