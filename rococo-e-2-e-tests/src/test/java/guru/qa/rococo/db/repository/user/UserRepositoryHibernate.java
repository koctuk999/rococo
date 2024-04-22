package guru.qa.rococo.db.repository.user;

import guru.qa.rococo.db.hibernate.JpaService;
import guru.qa.rococo.db.hibernate.ThreadLocalEntityManager;
import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.model.UserAuthEntity;
import guru.qa.rococo.db.model.UserDataEntity;
import io.qameta.allure.Step;
import jakarta.persistence.TypedQuery;
import lombok.SneakyThrows;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.rococo.db.Database.*;
import static guru.qa.rococo.db.hibernate.EmfProvider.INSTANCE;
import static guru.qa.rococo.utils.Helper.retryAction;

public class UserRepositoryHibernate extends JpaService implements UserRepository {

    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserRepositoryHibernate() {
        super(
                Map.of(
                        AUTH, new ThreadLocalEntityManager(INSTANCE.emf(AUTH)),
                        USERDATA, new ThreadLocalEntityManager(INSTANCE.emf(USERDATA))
                )
        );
    }

    @Override
    public UserAuthEntity createInAuth(UserAuthEntity userAuthEntity) {
        userAuthEntity.setPassword(pe.encode(userAuthEntity.getPassword()));
        persist(AUTH, userAuthEntity);
        return userAuthEntity;
    }

    @Override
    public UserDataEntity createInData(UserDataEntity userDataEntity) {
        persist(USERDATA, userDataEntity);
        return userDataEntity;
    }

    @Override
    public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
        return Optional.of(entityManager(AUTH).find(UserAuthEntity.class, id));
    }

    @Override
    public Optional<UserDataEntity> findByIdInUserdata(UUID id) {
        return Optional.of(entityManager(USERDATA).find(UserDataEntity.class, id));
    }

    @Override
    @SneakyThrows
    @Step("Find user by username {0} in auth")
    public UserAuthEntity findByUsernameInAuth(String username) {
        TypedQuery<UserAuthEntity> query = entityManager(AUTH)
                .createQuery(
                        "SELECT u FROM UserAuthEntity u WHERE u.username = :username",
                        UserAuthEntity.class
                )
                .setParameter("username", username);
        return retryAction(
                10,
                1000,
                () -> query.getSingleResult()
        );
    }

    @Override
    @SneakyThrows
    @Step("Find user by username {0} in userdata")
    public UserDataEntity findByUsernameInUserdata(String username) {
        TypedQuery<UserDataEntity> query = entityManager(USERDATA)
                .createQuery(
                        "SELECT u FROM UserDataEntity u WHERE u.username = :username",
                        UserDataEntity.class
                )
                .setParameter("username", username);
        return retryAction(
                10,
                1000,
                () -> query.getSingleResult()
        );
    }

    @Override
    public void deleteInAuth(UUID id) {
        UserAuthEntity userAuthEntity = findByIdInAuth(id).get();
        remove(AUTH, userAuthEntity);
    }

    @Override
    public void deleteInData(UUID id) {
        UserDataEntity userDataEntity = findByIdInUserdata(id).get();
        remove(USERDATA, userDataEntity);
    }
}
