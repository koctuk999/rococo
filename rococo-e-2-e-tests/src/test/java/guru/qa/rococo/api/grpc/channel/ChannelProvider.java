package guru.qa.rococo.api.grpc.channel;

import guru.qa.rococo.utils.GrpcLogInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ChannelProvider {
    INSTANCE;
    private final Map<Pair<String, Integer>, ManagedChannel> store = new ConcurrentHashMap<>();

    public ManagedChannel channel(Pair<String, Integer> grpcAddress) {
        return store.computeIfAbsent(grpcAddress, channel -> ManagedChannelBuilder
                .forAddress(grpcAddress.getKey(), grpcAddress.getValue())
                .intercept(new AllureGrpc(), new GrpcLogInterceptor())
                .usePlaintext()
                .build()
        );
    }

    public Collection<ManagedChannel> storedChannels() {
        return store.values();
    }
}
