package guru.qa.rococo.controller;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import guru.qa.rococo.service.api.GrpcUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final GrpcUserApi userApi;

    @Autowired
    public UserController(GrpcUserApi userApi) {
        this.userApi = userApi;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public String getUser(@AuthenticationPrincipal Jwt principal) throws InvalidProtocolBufferException {
        String username = principal.getClaimAsString("sub");
        return JsonFormat
                .printer()
                .print(userApi.getUser(username));
    }

    @PatchMapping
    public void patchUser() {
    }

}
