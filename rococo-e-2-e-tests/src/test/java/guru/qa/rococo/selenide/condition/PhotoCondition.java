package guru.qa.rococo.selenide.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.conditions.Not;
import org.openqa.selenium.WebElement;
import javax.annotation.Nonnull;
import static guru.qa.rococo.utils.ImageHelper.getPhotoByPath;


public class PhotoCondition {

    public static Condition imageCondition(String expectedPhoto, boolean byPath) {
        return new Condition("image") {
            @Nonnull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                String image = byPath? getPhotoByPath(expectedPhoto) :expectedPhoto;
                boolean matched = element
                            .getAttribute("src")
                            .equals(image);

                return new CheckResult(matched, matched ? "avatars are same" : "avatars are different");
            }

            @Nonnull
            @Override
            public Condition negate() {
                return new Not(this, true);
            }
        };
    }
}
