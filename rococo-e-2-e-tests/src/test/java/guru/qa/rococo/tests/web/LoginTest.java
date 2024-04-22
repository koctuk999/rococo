package guru.qa.rococo.tests.web;

import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.db.model.TestUser;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.*;

@DisplayName("Login tests")
@Tags({@Tag(AUTH_ACCEPTANCE), @Tag(USERDATA_ACCEPTANCE), @Tag(CLIENT_ACCEPTANCE)})
public class LoginTest extends BaseWebTest {

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
}
