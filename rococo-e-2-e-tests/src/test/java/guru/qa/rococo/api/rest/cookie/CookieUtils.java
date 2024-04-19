package guru.qa.rococo.api.rest.cookie;

import guru.qa.rococo.core.extensions.ContextHolderExtension;
import org.openqa.selenium.Cookie;

import static guru.qa.rococo.core.extensions.ApiLoginExtension.API_LOGIN_NAMESPACE;

public class CookieUtils {

    public static void setCodeVerifier(String codeVerifier) {
        ContextHolderExtension.Holder.INSTANCE.context()
                .getStore(API_LOGIN_NAMESPACE)
                .put("code_verifier", codeVerifier);
    }

    public static void setCodeChallenge(String codeChallenge) {
        ContextHolderExtension.Holder.INSTANCE.context()
                .getStore(API_LOGIN_NAMESPACE)
                .put("code_challenge", codeChallenge);
    }

    public static void setCode(String code) {
        ContextHolderExtension.Holder.INSTANCE.context()
                .getStore(API_LOGIN_NAMESPACE)
                .put("code", code);
    }

    public static void setToken(String token) {
        ContextHolderExtension.Holder.INSTANCE.context()
                .getStore(API_LOGIN_NAMESPACE)
                .put("token", token);
    }

    public static String getCodeVerifier() {
        return ContextHolderExtension.Holder.INSTANCE.context()
                .getStore(API_LOGIN_NAMESPACE)
                .get("code_verifier", String.class);
    }

    public static String getCodChallenge() {
        return ContextHolderExtension.Holder.INSTANCE.context()
                .getStore(API_LOGIN_NAMESPACE)
                .get("code_challenge", String.class);
    }

    public static String getCode() {
        return ContextHolderExtension.Holder.INSTANCE.context()
                .getStore(API_LOGIN_NAMESPACE)
                .get("code", String.class);
    }

    public static String getToken() {
        return ContextHolderExtension.Holder.INSTANCE.context()
                .getStore(API_LOGIN_NAMESPACE)
                .get("token", String.class);
    }

    public static String getCsrfToken() {
        return ThreadSafeCookieManager
                .INSTANCE
                .getCookieValue("XSRF-TOKEN");
    }

    public static Cookie jsessionCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieManager.INSTANCE.getCookieValue("JSESSIONID")
        );
    }
}
