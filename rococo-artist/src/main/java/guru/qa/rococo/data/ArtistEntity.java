package guru.qa.rococo.data;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Data
@Table(name = "artist")
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "biography", nullable = false)
    private String biography;

    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] photo;

    public ArtistEntity(UUID id, String name, String biography, byte[] photo) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.photo = photo;
    }

    public ArtistEntity() {
    }
}
