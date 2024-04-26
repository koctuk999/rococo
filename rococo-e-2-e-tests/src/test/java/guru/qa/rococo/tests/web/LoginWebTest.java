package guru.qa.rococo.tests.web;

import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.db.model.TestUser;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.*;
import static guru.qa.rococo.page.component.message.ErrorMessage.LOGIN_ERROR;
import static guru.qa.rococo.utils.RandomUtils.genRandomUsername;
import static guru.qa.rococo.utils.RandomUtils.generateRandomPassword;

@DisplayName("Login web tests")
@Tags({@Tag(AUTH_ACCEPTANCE), @Tag(USERDATA_ACCEPTANCE), @Tag(CLIENT_ACCEPTANCE)})
public class LoginWebTest extends BaseWebTest {

    @Test
    @CreatedUser
    @DisplayName("Success login test")
    public void successLogin(TestUser user) {
        mainPage
                .open()
                .toLogin()
                .signIn(user.username(), user.password());

        mainPage
                .waitForPageLoaded()
                .getHeader()
                .checkLoggedIn();
    }

    @Test
    @DisplayName("Error login test [user is not exists]")
    void userIsNotExist() {
        mainPage
                .open()
                .toLogin()
                .setUsername(genRandomUsername())
                .setPassword(generateRandomPassword())
                .errorSubmit()
                .checkErrorMessage(LOGIN_ERROR);
    }

    @Test
    @CreatedUser
    @DisplayName("Error login test [password is wrong]")
    void passwordIsWrong(TestUser user) {
        mainPage
                .open()
                .toLogin()
                .setUsername(user.username())
                .setPassword(generateRandomPassword())
                .errorSubmit()
                .checkErrorMessage(LOGIN_ERROR);
    }
}
