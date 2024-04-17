package guru.qa.rococo.db.model;

import guru.qa.grpc.rococo.grpc.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
@Entity
@Builder
@Data
@Table(name = "\"user\"")
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

    public UserDataEntity() {
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserDataEntity that = (UserDataEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public User toGrpc() {
        String firstname = this.getFirstname() == null ? "" : this.getFirstname();
        String lastname = this.getLastname() == null ? "" : this.getLastname();
        String avatar = this.getAvatar() == null ? ""
                : new String(this.getAvatar(), UTF_8);
        return User
                .newBuilder()
                .setId(this.getId().toString())
                .setUsername(this.getUsername())
                .setFirstname(firstname)
                .setLastname(lastname)
                .setAvatar(avatar)
                .build();
    }
}
