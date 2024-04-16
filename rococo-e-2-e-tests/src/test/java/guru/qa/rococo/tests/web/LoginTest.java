package guru.qa.rococo.tests.web;

import guru.qa.rococo.core.annotations.CreateUser;
import guru.qa.rococo.db.model.TestUser;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Login tests")
public class LoginTest extends BaseWebTest {

    @Test
    @CreateUser
    @DisplayName("Success login")
    public void signIn(TestUser user) {
        mainPage
                .open()
                .getHeader()
                .toLoginPage()
                .setUsername(user.username())
                .setPassword(user.password())
                .submit();

        mainPage
                .waitForPageLoaded()
                .getHeader()
                .checkLoggedIn();

    }
}
