package guru.qa.rococo.data;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaintingRepository extends JpaRepository<PaintingEntity, UUID> {
    Page<PaintingEntity> findAll(Pageable pageable);

    @Nonnull
    Page<PaintingEntity> findAllByTitleContainsIgnoreCase(
            @Nonnull String title,
            @Nonnull Pageable pageable
    );

    @Nonnull
    Page<PaintingEntity> findAllByArtistId(
            @Nonnull UUID artistId,
            @Nonnull Pageable pageable
    );

    Optional<PaintingEntity> findById(UUID uuid);
}
