package guru.qa.rococo.api.rest.interceptor;

import guru.qa.rococo.core.extensions.ApiLoginExtension;
import guru.qa.rococo.core.extensions.ContextHolderExtension;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Converter;

import java.io.IOException;

import static guru.qa.rococo.api.rest.cookie.CookieUtils.setCode;

public class CodeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        if (response.isRedirect()) {
            final String location = response.header("Location");
            if (location.contains("code=")) {
                final String code = StringUtils.substringAfter(location, "code=");
                setCode(code);
            }
        }
        return response;
    }
}
