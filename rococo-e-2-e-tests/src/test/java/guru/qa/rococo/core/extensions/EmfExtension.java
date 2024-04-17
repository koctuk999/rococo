package guru.qa.rococo.core.extensions;

import guru.qa.rococo.db.hibernate.EmfProvider;
import jakarta.persistence.EntityManagerFactory;

public class EmfExtension implements SuiteExtension {
    @Override
    public void afterSuite() {
        EmfProvider.INSTANCE.storedEmf().forEach(EntityManagerFactory::close);
    }
}
