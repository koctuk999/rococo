package guru.qa.rococo.api.rest;

import guru.qa.rococo.api.rest.cookie.ThreadSafeCookieManager;
import guru.qa.rococo.config.Config;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.net.CookieManager;

import static guru.qa.rococo.api.rest.cookie.ThreadSafeCookieManager.INSTANCE;
import static guru.qa.rococo.config.Config.getInstance;
import static java.net.CookiePolicy.ACCEPT_ALL;

public abstract class RestClient {
    protected static final Config CFG = getInstance();

    protected final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    public RestClient(
            @Nonnull String baseUrl,
            boolean followRedirect,
            @Nonnull Converter.Factory converter,
            @Nullable Interceptor... interceptors
    ) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }
        builder.addNetworkInterceptor(
                new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
        );

        builder.followRedirects(followRedirect);
        builder.cookieJar(
                new JavaNetCookieJar(
                        new CookieManager(INSTANCE, ACCEPT_ALL)
                )
        );

        this.okHttpClient = builder.build();
        this.retrofit = new Retrofit.Builder()
                .client(this.okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build();
    }

    public RestClient(
            @Nonnull String baseUri,
            boolean followRedirect,
            @Nullable Interceptor... interceptors
    ) {
        this(
                baseUri,
                followRedirect,
                JacksonConverterFactory.create(),
                interceptors
        );
    }
}
