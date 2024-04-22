package guru.qa.rococo.db.model;

import guru.qa.grpc.rococo.grpc.Museum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static java.util.UUID.fromString;

@Getter
@Setter
@Entity
@Builder
@Data
@Table(name = "museum")
public class MuseumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] photo;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country_id")
    private UUID countryId;

    public MuseumEntity() {
    }

    public MuseumEntity(UUID id, String title, String description, byte[] photo, String city, UUID countryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.city = city;
        this.countryId = countryId;
    }

    public static MuseumEntity toMuseumEntity(Museum museum) {
        return MuseumEntity.builder()
                .id(fromString(museum.getId()))
                .title(museum.getTitle())
                .description(museum.getDescription())
                .photo(museum.getPhoto().getBytes())
                .city(museum.getGeo().getCity())
                .countryId(fromString(museum.getGeo().getCountry().getId()))
                .build();
    }
}

