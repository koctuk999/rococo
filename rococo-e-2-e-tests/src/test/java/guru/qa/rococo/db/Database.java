package guru.qa.rococo.db;

import guru.qa.rococo.config.Config;
import lombok.RequiredArgsConstructor;

import static guru.qa.rococo.config.Config.getInstance;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substringAfter;

@RequiredArgsConstructor
public enum Database {
    AUTH("jdbc:postgresql://%s:%d/rococo-auth"),
    USERDATA("jdbc:postgresql://%s:%d/rococo-userdata"),
    COUNTRY("jdbc:postgresql://%s:%d/rococo-country"),
    PAINTING("jdbc:postgresql://%s:%d/rococo-painting"),
    MUSEUM("jdbc:postgresql://%s:%d/rococo-museum"),
    ARTIST("jdbc:postgresql://%s:%d/rococo-artist");

    private final String url;
    private static final Config cfg = getInstance();

    public String getUrl() {
        return format(
                url,
                cfg.dbUrl(),
                cfg.dbPort()
        );
    }

    public String p6spyUrl() {
        return "jdbc:p6spy:" + substringAfter(getUrl(), "jdbc:");
    }


}
