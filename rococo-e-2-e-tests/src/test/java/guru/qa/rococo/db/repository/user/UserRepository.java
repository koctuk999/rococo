package guru.qa.rococo.db.repository.user;

import guru.qa.rococo.db.model.UserAuthEntity;
import guru.qa.rococo.db.model.UserDataEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createInAuth(UserAuthEntity userAuthEntity);

    UserDataEntity createInData(UserDataEntity userDataEntity);

    Optional<UserAuthEntity> findByIdInAuth(UUID id);

    Optional<UserDataEntity> findByIdInUserdata(UUID id);

    UserAuthEntity findByUsernameInAuth(String username);

    UserDataEntity findByUsernameInUserdata(String username);

    void deleteInAuth(UUID id);

    void deleteInData(UUID id);
}
