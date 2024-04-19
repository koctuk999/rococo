package guru.qa.rococo.core.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static guru.qa.rococo.core.extensions.ContextHolderExtension.Holder.INSTANCE;

public class ContextHolderExtension implements BeforeEachCallback, AfterEachCallback {
  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    INSTANCE.clear();
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    INSTANCE.setContext(extensionContext);
  }

  public enum Holder {
    INSTANCE;

    private final ThreadLocal<ExtensionContext> contextHolder = new ThreadLocal<>();

    public void setContext(ExtensionContext context) {
      contextHolder.set(context);
    }

    public ExtensionContext context() {
      return contextHolder.get();
    }

    public void clear() {
      contextHolder.remove();
    }
  }
}
