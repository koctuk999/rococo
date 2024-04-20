package guru.qa.rococo.db.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
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
}

