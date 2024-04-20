package guru.qa.rococo.selenide.selector;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byAttribute;

public class CustomSelectors {

    public static By byDataTestId(String testId) {
        return byAttribute("data-testid", testId);
    }
}
