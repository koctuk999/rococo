package guru.qa.rococo.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat.Printer;
import io.grpc.*;
import org.slf4j.Logger;

import static com.google.protobuf.util.JsonFormat.printer;
import static org.slf4j.LoggerFactory.getLogger;

public class GrpcLogInterceptor implements ClientInterceptor {

    private static final Logger LOGGER = getLogger(GrpcLogInterceptor.class);
    private static final Printer JSON_PRINTER = printer();

    @Override
    public <T, A> ClientCall<T, A> interceptCall(MethodDescriptor<T, A> method,
                                                 CallOptions callOptions,
                                                 Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(
                next.newCall(method, callOptions.withoutWaitForReady())) {

            @Override
            public void sendMessage(T message) {
                LOGGER.info("Send gRPC request to %s%s".formatted(next.authority(), trimGrpcMethodName(method.getFullMethodName())));
                try {
                    LOGGER.info("Request body:\n %s".formatted(JSON_PRINTER.print((MessageOrBuilder) message)));
                } catch (InvalidProtocolBufferException e) {
                    LOGGER.error("Unable to transform message -> json");
                }
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<A> responseListener, Metadata headers) {
                final Listener<A> listener = new ForwardingClientCallListener<>() {
                    @Override
                    protected Listener<A> delegate() {
                        return responseListener;
                    }

                    @Override
                    public void onClose(io.grpc.Status status, Metadata trailers) {
                        super.onClose(status, trailers);
                    }

                    @Override
                    public void onMessage(A message) {
                        try {
                            LOGGER.info("response body:\n %s".formatted(JSON_PRINTER.print((MessageOrBuilder) message)));
                        } catch (InvalidProtocolBufferException e) {
                            LOGGER.warn("Can`t parse gRPC response", e);
                        }
                        super.onMessage(message);
                    }
                };
                super.start(listener, headers);
            }

            private String trimGrpcMethodName(final String source) {
                return source.substring(source.lastIndexOf('/'));
            }
        };
    }
}
