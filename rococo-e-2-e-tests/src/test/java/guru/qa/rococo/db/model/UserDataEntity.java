package guru.qa.rococo.db.model;

import guru.qa.grpc.rococo.grpc.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;


@Entity
@Builder
@Data
@Table(name = "\"user\"")
@NoArgsConstructor
public class UserDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column()
    private String firstname;

    @Column()
    private String lastname;

    @Column(columnDefinition = "bytea")
    private byte[] avatar;

    public UserDataEntity(UUID id, String username, String firstname, String lastname, byte[] avatar) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.avatar = avatar;
    }
}
