package guru.qa.rococo.db.hibernate;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.db.Database;
import jakarta.persistence.EntityManagerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public enum EmfProvider {
    INSTANCE;

    private static final Config CFG = Config.getInstance();

    private final Map<Database, EntityManagerFactory> store = new ConcurrentHashMap<>();

    public EntityManagerFactory emf(Database database) {
        return store.computeIfAbsent(database, k -> {
            Map<String, String> settings = new HashMap<>();
            settings.put("hibernate.connection.url", k.p6spyUrl());
            settings.put("hibernate.connection.user", CFG.dbUsername());
            settings.put("hibernate.connection.password", CFG.dbSecret());
            settings.put("hibernate.connection.driver_class", "com.p6spy.engine.spy.P6SpyDriver");
            settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            return createEntityManagerFactory("rococo", settings);
        });
    }

    public Collection<EntityManagerFactory> storedEmf() {
        return store.values();
    }
}
