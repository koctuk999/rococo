package guru.qa.rococo.core.extensions;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

import static org.openqa.selenium.OutputType.BYTES;

public class BrowserExtension implements AfterEachCallback, TestExecutionExceptionHandler {
    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        if (WebDriverRunner.hasWebDriverStarted()) Selenide.closeWebDriver();
    }

    @Override
    public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
        if (WebDriverRunner.hasWebDriverStarted()) {
            ByteArrayInputStream screenshot = new ByteArrayInputStream((
                    (TakesScreenshot) WebDriverRunner.getWebDriver()
            ).getScreenshotAs(BYTES));
            Allure.addAttachment("Screenshot after test", screenshot);
        }
        throw throwable;
    }
}
