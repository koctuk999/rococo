package guru.qa.rococo.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.protobuf.InvalidProtocolBufferException;
import guru.qa.grpc.rococo.grpc.User;
import guru.qa.rococo.service.api.GrpcUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static guru.qa.rococo.utils.Hellpers.convertGrpcToJson;
import static guru.qa.rococo.utils.Hellpers.convertJsonToGrpc;

//TODO унести в сервис?
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final GrpcUserApi userApi;

    @Autowired
    public UserController(GrpcUserApi userApi) {
        this.userApi = userApi;
    }

    @GetMapping
    public JsonNode getUser(@AuthenticationPrincipal Jwt principal) throws InvalidProtocolBufferException {
        String username = principal.getSubject();
        return convertGrpcToJson(userApi.getUser(username));
    }

    @PatchMapping
    public JsonNode patchUser(@RequestBody JsonNode request) {
        User user = userApi.updateUser(convertJsonToGrpc(request, User.class));
        return convertGrpcToJson(user);
    }

}
