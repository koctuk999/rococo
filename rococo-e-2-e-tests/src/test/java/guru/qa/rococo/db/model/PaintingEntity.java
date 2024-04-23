package guru.qa.rococo.db.model;

import guru.qa.grpc.rococo.grpc.Painting;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;
import static java.util.UUID.fromString;

@Getter
@Setter
@Entity
@Data
@Builder
@Table(name = "painting")
public class PaintingEntity {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] content;

    @Column(name = "museum_id")
    private UUID museumId;

    @Column(name = "artist_id")
    private UUID artistId;

    public PaintingEntity(UUID id, String title, String description, byte[] content, UUID museumId, UUID artistId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.museumId = museumId;
        this.artistId = artistId;
    }

    public PaintingEntity() {
    }

    public static PaintingEntity toPaintingEntity(Painting painting) {
        return PaintingEntity.builder()
                .id(painting.getId().isEmpty() ? null : fromString(painting.getId()))
                .title(painting.getTitle())
                .description(painting.getDescription())
                .content(painting.getContent().getBytes())
                .artistId(fromString(painting.getArtist().getId()))
                .museumId(fromString(painting.getMuseum().getId()))
                .build();
    }
}
