package guru.qa.rococo.tests.web;

import guru.qa.rococo.db.model.UserAuthEntity;
import guru.qa.rococo.db.model.UserDataEntity;
import guru.qa.rococo.db.repository.user.UserRepository;
import guru.qa.rococo.db.repository.user.UserRepositoryHibernate;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.*;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.RandomUtils.genRandomUsername;
import static guru.qa.rococo.utils.RandomUtils.generateRandomPassword;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Registration tests")
@Tags({@Tag(AUTH_ACCEPTANCE), @Tag(USERDATA_ACCEPTANCE), @Tag(CLIENT_ACCEPTANCE)})
public class RegisterTest extends BaseWebTest {
    UserRepository userRepository = new UserRepositoryHibernate();

    @Test
    @DisplayName("Success registration test")
    public void successRegistration() {
        String username = genRandomUsername();
        String password = generateRandomPassword();

        mainPage
                .open()
                .toLogin()
                .toRegister()
                .registerUser(username, password);

        UserAuthEntity userAuthEntity = userRepository.findByUsernameInAuth(username);
        check("user exists in auth",
                userAuthEntity.getUsername(), equalTo(username));

        UserDataEntity userDataEntity = userRepository.findByUsernameInUserdata(username);
        check("user exists in userdata",
                userDataEntity.getUsername(), equalTo(username));


    }
}
