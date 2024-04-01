package guru.qa.rococo.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
@Data
@Table(name = "painting")
@Builder
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

    public PaintingEntity() {
    }

    public PaintingEntity(UUID id, String title, String description, byte[] content, UUID museumId, UUID artistId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.museumId = museumId;
        this.artistId = artistId;
    }
}
