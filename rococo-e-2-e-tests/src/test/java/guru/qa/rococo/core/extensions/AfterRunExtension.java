package guru.qa.rococo.core.extensions;

import guru.qa.rococo.api.grpc.channel.ChannelProvider;
import guru.qa.rococo.db.hibernate.EmfProvider;
import io.grpc.ManagedChannel;
import jakarta.persistence.EntityManagerFactory;

public class AfterRunExtension implements SuiteExtension {
    @Override
    public void afterSuite() {
        EmfProvider
                .INSTANCE
                .storedEmf()
                .forEach(EntityManagerFactory::close);

        ChannelProvider
                .INSTANCE
                .storedChannels()
                .forEach(ManagedChannel::shutdown);
    }
}
