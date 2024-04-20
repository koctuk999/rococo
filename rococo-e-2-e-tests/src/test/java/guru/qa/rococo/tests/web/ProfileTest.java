package guru.qa.rococo.tests.web;

import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.db.model.TestUser;
import guru.qa.rococo.tests.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.core.TestTag.CLIENT_ACCEPTANCE;
import static guru.qa.rococo.core.TestTag.USERDATA_ACCEPTANCE;
import static guru.qa.rococo.page.component.message.SuccessMessage.PROFILE_UPDATED;
import static guru.qa.rococo.utils.ImageHelper.USER_PHOTO_PATH;
import static guru.qa.rococo.utils.RandomUtils.genRandomLastName;
import static guru.qa.rococo.utils.RandomUtils.genRandomName;

@DisplayName("Profile tests")
@Tags({@Tag(CLIENT_ACCEPTANCE), @Tag(USERDATA_ACCEPTANCE)})
public class ProfileTest extends BaseWebTest {

    @DisplayName("Check user's profile")
    @Test
    @LoggedIn(user = @CreatedUser)
    public void getProfile(TestUser user) {
        mainPage
                .getHeader()
                .checkLoggedIn();

        mainPage
                .getHeader()
                .toProfile()
                .checkUsername(user.username());
    }

    @DisplayName("Check update user's data")
    @Test
    @LoggedIn(user = @CreatedUser)
    public void updateProfile(TestUser user) {
        String imagePath = USER_PHOTO_PATH;
        String firstname = genRandomName();
        String surname = genRandomLastName();
        mainPage
                .getHeader()
                .toProfile()
                .setAvatar(imagePath)
                .setFirstnameInput(firstname)
                .setSurnameInput(surname)
                .update();

        mainPage
                .checkToasterMessage(PROFILE_UPDATED)
                .closeToast()
                .getHeader()
                .checkAvatar(imagePath);

        mainPage
                .getHeader()
                .toProfile()
                .checkUsername(user.username())
                .checkFirstname(firstname)
                .checkSurname(surname)
                .checkAvatar(imagePath);

    }
}
