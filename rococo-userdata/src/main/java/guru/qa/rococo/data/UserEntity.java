package guru.qa.rococo.data;

import guru.qa.grpc.rococo.grpc.User;
import io.grpc.netty.shaded.io.netty.util.CharsetUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class UserEntity {
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
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
                : new String(this.getAvatar(), UTF_8)
                .substring(1); //TODO убрать костыль
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
