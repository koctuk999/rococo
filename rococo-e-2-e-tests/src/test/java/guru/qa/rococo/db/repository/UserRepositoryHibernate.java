package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.hibernate.EmfProvider;
import guru.qa.rococo.db.hibernate.JpaService;
import guru.qa.rococo.db.hibernate.ThreadLocalEntityManager;
import guru.qa.rococo.db.model.UserAuthEntity;
import guru.qa.rococo.db.model.UserDataEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.rococo.db.Database.AUTH;
import static guru.qa.rococo.db.Database.USERDATA;
import static guru.qa.rococo.db.hibernate.EmfProvider.INSTANCE;

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
