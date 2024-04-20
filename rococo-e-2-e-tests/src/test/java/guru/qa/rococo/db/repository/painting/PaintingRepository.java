package guru.qa.rococo.db.repository.painting;

import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.model.PaintingEntity;

public interface PaintingRepository {
    PaintingEntity findPaintingByTitle(String title);
}
