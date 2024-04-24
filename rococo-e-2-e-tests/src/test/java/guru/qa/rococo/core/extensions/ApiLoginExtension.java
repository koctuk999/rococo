package guru.qa.rococo.core.extensions;

import com.codeborne.selenide.LocalStorage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SessionStorage;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.rococo.api.rest.auth.AuthClient;
import guru.qa.rococo.api.rest.cookie.ThreadSafeCookieManager;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.core.annotations.Token;
import guru.qa.rococo.db.model.TestUser;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.util.Optional;

import static guru.qa.rococo.api.rest.cookie.CookieUtils.*;
import static guru.qa.rococo.core.extensions.CreateUserExtension.CREATE_USER_NAMESPACE;
import static guru.qa.rococo.utils.OauthUtils.generateCodeChallange;
import static guru.qa.rococo.utils.OauthUtils.generateCodeVerifier;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    private static final Config CFG = Config.getInstance();
    private final AuthClient authClient = new AuthClient();

    public static final Namespace API_LOGIN_NAMESPACE = create(ApiLoginExtension.class);


    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<LoggedIn> annotation = findAnnotation(
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
            authClient.doLogin(username, password);

            if (annotation.get().setCookies()){
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
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return findAnnotation(parameterContext.getParameter(), Token.class).isPresent()
                && parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(String.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return "Bearer %s".formatted(getToken());
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        ThreadSafeCookieManager.INSTANCE.removeAll();
    }
}
