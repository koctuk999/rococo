package guru.qa.rococo.db.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity
@Builder
@Table(name = "\"user\"")
@NoArgsConstructor
public class UserAuthEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired;

    @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<AuthorityEntity> authorities = new ArrayList<>();

    public UserAuthEntity(UUID id, String username, String password, Boolean enabled, Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired, List<AuthorityEntity> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = authorities;
    }

    public void addAuthorities(AuthorityEntity... authorities) {
        for (AuthorityEntity authority : authorities) {
            this.authorities.add(authority);
            authority.setUser(this);
        }
    }

    public void removeAuthority(AuthorityEntity authority) {
        this.authorities.remove(authority);
        authority.setUser(null);
    }
}
