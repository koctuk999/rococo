package guru.qa.rococo.core.extensions;

import com.codeborne.selenide.LocalStorage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SessionStorage;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.rococo.api.rest.auth.AuthApiClient;
import guru.qa.rococo.api.rest.cookie.ThreadSafeCookieManager;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.db.model.TestUser;
import guru.qa.rococo.page.BasePage;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.rococo.api.rest.cookie.CookieUtils.*;
import static guru.qa.rococo.core.extensions.CreateUserExtension.CREATE_USER_NAMESPACE;
import static guru.qa.rococo.utils.OauthUtils.generateCodeChallange;
import static guru.qa.rococo.utils.OauthUtils.generateCodeVerifier;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback {

    private static final Config CFG = Config.getInstance();
    private final AuthApiClient authApiClient = new AuthApiClient();

    public static final Namespace API_LOGIN_NAMESPACE = create(ApiLoginExtension.class);


    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<LoggedIn> annotation = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(), LoggedIn.class
        );

        if (annotation.isPresent()) {
            TestUser user = extensionContext
                    .getStore(CREATE_USER_NAMESPACE)
                    .get(extensionContext.getUniqueId(), TestUser.class);

            String username = user.username();
            String password = user.password();

            final String codeVerifier = generateCodeVerifier();
            final String codeChallenge = generateCodeChallange(codeVerifier);
            setCodeVerifier(codeVerifier);
            setCodeChallenge(codeChallenge);
            authApiClient.doLogin(username, password);

            Selenide.open(CFG.frontUrl());

            SessionStorage sessionStorage = Selenide.sessionStorage();
            sessionStorage.setItem("codeChallenge", getCodChallenge());
            sessionStorage.setItem("id_token", getToken());
            sessionStorage.setItem("codeVerifier", getCodeVerifier());

            LocalStorage localStorage = Selenide.localStorage();
            localStorage.setItem("codeChallenge", getCodChallenge());
            localStorage.setItem("id_token", getToken());
            localStorage.setItem("codeVerifier", getCodeVerifier());

            WebDriverRunner
                    .getWebDriver()
                    .manage()
                    .addCookie(jsessionCookie());

            Selenide.refresh();
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        ThreadSafeCookieManager.INSTANCE.removeAll();
    }
}
