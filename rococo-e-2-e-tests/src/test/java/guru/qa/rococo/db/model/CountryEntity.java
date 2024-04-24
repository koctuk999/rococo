package guru.qa.rococo.db.model;

import guru.qa.grpc.rococo.grpc.Country;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;


@Entity
@Data
@Builder
@Table(name = "country")
@NoArgsConstructor
public class CountryEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue()
    private UUID id;

    @Column(name = "country")
    private String countryName;

    @Column(name = "country_code")
    private String countryCode;

    public CountryEntity(UUID id, String countryName, String countryCode) {
        this.id = id;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }
}
