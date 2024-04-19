package guru.qa.rococo.api.rest.cookie;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

import static java.lang.ThreadLocal.withInitial;
import static java.net.CookiePolicy.ACCEPT_ALL;

public enum ThreadSafeCookieManager implements CookieStore {
    INSTANCE;

    ThreadLocal<CookieStore> tlCookieManager = withInitial(
            () -> new CookieManager(null, ACCEPT_ALL).getCookieStore()
    );

    private CookieStore getCookieStore() {
        return tlCookieManager.get();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        getCookieStore().add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return getCookieStore().get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return getCookieStore().getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return getCookieStore().getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return getCookieStore().remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return getCookieStore().removeAll();
    }

    public String getCookieValue(String cookieName) {
        return getCookies()
                .stream()
                .filter(c -> c.getName().equals(cookieName))
                .map(HttpCookie::getValue)
                .findFirst()
                .orElseThrow();
    }
}
