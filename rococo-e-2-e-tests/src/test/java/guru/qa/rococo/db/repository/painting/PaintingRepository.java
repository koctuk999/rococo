package guru.qa.rococo.db.repository.painting;

import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.model.PaintingEntity;

import java.util.UUID;

public interface PaintingRepository {
    PaintingEntity findPaintingByTitle(String title);
    PaintingEntity findPaintingById(UUID id);
}
