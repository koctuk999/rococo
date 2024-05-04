package guru.qa.rococo.tests.web;

import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.db.model.TestUser;
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
import static guru.qa.rococo.page.component.message.ErrorMessage.*;
import static guru.qa.rococo.utils.CustomAssert.check;
import static guru.qa.rococo.utils.RandomUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Registration web tests")
@Tags({@Tag(AUTH_ACCEPTANCE), @Tag(USERDATA_ACCEPTANCE), @Tag(CLIENT_ACCEPTANCE)})
public class RegisterWebTest extends BaseWebTest {
    private UserRepository userRepository = new UserRepositoryHibernate();

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

    @Test
    @DisplayName("Error registration test [wrong submit password]")
    public void wrongPassword() {
        String username = genRandomUsername();
        String password = generateRandomPassword();
        String wrongPassword = generateRandomPassword();

        mainPage
                .open()
                .toLogin()
                .toRegister()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(wrongPassword)
                .errorSubmit()
                .checkErrorMessage(PASSWORD_NOT_EQUAL);
    }

    @Test
    @CreatedUser
    @DisplayName("Error registration test [username is already exists]")
    public void usernameAlreadyExist(TestUser user) {
        mainPage
                .open()
                .toLogin()
                .toRegister()
                .setUsername(user.username())
                .setPassword(user.password())
                .setPasswordSubmit(user.password())
                .errorSubmit()
                .checkErrorMessage(USERNAME_ALREADY_EXISTS, user.username());
    }

    @Test
    @DisplayName("Error registration test [password to short]")
    public void passwordToShort() {
        String username = genRandomUsername();
        String password = randomAlphabetic(2);
        mainPage
                .open()
                .toLogin()
                .toRegister()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .errorSubmit()
                .checkErrorMessage(PASSWORD_NOT_VALID);
    }

    @Test
    @DisplayName("Error registration test [password to long]")
    public void passwordToLong() {
        String username = genRandomUsername();
        String password = randomAlphabetic(13);
        mainPage
                .open()
                .toLogin()
                .toRegister()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .errorSubmit()
                .checkErrorMessage(PASSWORD_NOT_VALID);
    }
}
