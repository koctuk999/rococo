package guru.qa.rococo.tests.web;

import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.db.model.TestUser;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Profile tests")
public class ProfileTest extends BaseWebTest {

    @DisplayName("Check user's profile")
    @Test
    @LoggedIn(user = @CreatedUser)
    public void checkProfile(TestUser user) {
        mainPage
                .getHeader()
                .checkLoggedIn();
    }
}
