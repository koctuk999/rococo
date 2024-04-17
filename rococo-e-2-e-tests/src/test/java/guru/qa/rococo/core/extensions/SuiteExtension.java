package guru.qa.rococo.core.extensions;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public interface SuiteExtension extends BeforeAllCallback {

  @Override
  default void beforeAll(ExtensionContext extensionContext) throws Exception {
    extensionContext.getRoot().getStore(GLOBAL)
        .getOrComputeIfAbsent(this.getClass(), key -> {
          beforeSuite(extensionContext);
          return new ExtensionContext.Store.CloseableResource() {
            @Override
            public void close() throws Throwable {
              afterSuite();
            }
          };
        });
  }

  default void beforeSuite(ExtensionContext extensionContext) {

  }

  default void afterSuite() {

  }
}
