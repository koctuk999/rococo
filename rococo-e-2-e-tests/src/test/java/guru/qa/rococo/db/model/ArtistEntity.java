package guru.qa.rococo.db.model;

import guru.qa.grpc.rococo.grpc.Artist;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "artist")
@NoArgsConstructor
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

    public static ArtistEntity toArtistEntity(Artist artist) {
        return ArtistEntity.builder()
                .id(UUID.fromString(artist.getId()))
                .name(artist.getName())
                .biography(artist.getBiography())
                .photo(artist.getPhoto().getBytes())
                .build();
    }
}
