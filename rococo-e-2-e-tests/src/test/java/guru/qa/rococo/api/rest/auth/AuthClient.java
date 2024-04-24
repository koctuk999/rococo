package guru.qa.rococo.api.rest.auth;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.api.rest.RestClient;
import guru.qa.rococo.api.rest.interceptor.CodeInterceptor;

import java.util.Base64;

import static guru.qa.rococo.api.rest.cookie.CookieUtils.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AuthClient extends RestClient {
    private final AuthApi authApi;

    public AuthClient() {
        super(
                CFG.authUrl(),
                true,
                new CodeInterceptor()
        );
        this.authApi = retrofit.create(AuthApi.class);
    }

    public void doLogin(String username, String password) throws Exception {
        authApi.authorize(
                "code",
                "client",
                "openid",
                "%s/authorized".formatted(CFG.frontUrl()),
                getCodChallenge(),
                "S256"
        ).execute();

        authApi.login(
                username,
                password,
                getCsrfToken()
        ).execute();

        JsonNode responseBody = authApi.token(
                        "Basic %s".formatted(new String(Base64.getEncoder().encode("client:secret".getBytes(UTF_8)))),
                        "client",
                        "%s/authorized".formatted(CFG.frontUrl()),
                        "authorization_code",
                        getCode(),
                        getCodeVerifier()
                )
                .execute()
                .body();

        final String token = responseBody.get("id_token").asText();
        setToken(token);
    }
}
