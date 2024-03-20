package guru.qa.rococo.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import guru.qa.rococo.config.keys.KeyManager;
import guru.qa.rococo.service.SpecificRequestDumperFilter;
import guru.qa.rococo.service.cors.CorsCustomizer;
import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Configuration
public class RococoAuthServiceConfig {

    private final KeyManager keyManager;
    private final String rococoFrontUri;
    private final String rococoAuthUri;
    private final String clientId;
    private final String clientSecret;
    private final CorsCustomizer corsCustomizer;
    private final String serverPort;
    private final String defaultHttpsPort = "443";
    private final Environment environment;

    @Autowired
    public RococoAuthServiceConfig(KeyManager keyManager,
                                   @Value("${rococo-front.base-uri}") String rococoFrontUri,
                                   @Value("${rococo-auth.base-uri}") String rococoAuthUri,
                                   @Value("${oauth2.client-id}") String clientId,
                                   @Value("${oauth2.client-secret}") String clientSecret,
                                   @Value("${server.port}") String serverPort,
                                   CorsCustomizer corsCustomizer,
                                   Environment environment) {
        this.keyManager = keyManager;
        this.rococoFrontUri = rococoFrontUri;
        this.rococoAuthUri = rococoAuthUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.serverPort = serverPort;
        this.corsCustomizer = corsCustomizer;
        this.environment = environment;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      LoginUrlAuthenticationEntryPoint entryPoint) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        //TODO вернуть для докера
        // if (environment.acceptsProfiles(Profiles.of("local", "staging"))) {
        http.addFilterBefore(new SpecificRequestDumperFilter(
                new RequestDumperFilter(),
                "/login", "/oauth2/.*"
        ), DisableEncodeUrlFilter.class);
        //}
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
        http.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(entryPoint))
                .oauth2ResourceServer(rs -> rs.jwt(Customizer.withDefaults()));
        corsCustomizer.corsCustomizer(http);
        return http.build();
    }

    @Bean
    //@Profile({"local", "docker"}) TODO вернуть для докера
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPointHttp() {
        return new LoginUrlAuthenticationEntryPoint("/login");
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(rococoFrontUri + "/authorized")
                .scope(OidcScopes.OPENID)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .refreshTokenTimeToLive(Duration.ofHours(10))
                        .build())
                .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(rococoAuthUri)
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
        JWKSet set = new JWKSet(keyManager.rsaKey());
        return (jwkSelector, securityContext) -> jwkSelector.select(set);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
}
