package guru.qa.rococo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import guru.qa.grpc.rococo.grpc.Museum;
import lombok.SneakyThrows;

public class Hellpers {
    @SneakyThrows
    public static JsonNode convertGrpcToJson(Message grpcObject) {
        ObjectMapper om = new ObjectMapper();
        return om.readTree(
                JsonFormat
                        .printer()
                        .print(grpcObject)
        );
    }

    @SneakyThrows
    public static <T extends Message> T convertJsonToGrpc(JsonNode json, Class<T> messageClass) {
        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(json);
        T.Builder builder = (T.Builder) messageClass.getMethod("newBuilder").invoke(null);
        JsonFormat.parser().merge(jsonString, builder);
        return (T) builder.build();
    }

}
