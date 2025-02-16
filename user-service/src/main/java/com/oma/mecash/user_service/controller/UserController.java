package com.oma.mecash.user_service.controller;

import com.oma.mecash.security_service.model.SignUpResponse;
import com.oma.mecash.user_service.dto.APIResponse;
import com.oma.mecash.user_service.dto.AccessTokenRequest;
import com.oma.mecash.user_service.dto.AccessTokenResponse;
import com.oma.mecash.user_service.dto.CreateUserDTO;
import com.oma.mecash.user_service.dto.UpdateUserDTO;
import com.oma.mecash.user_service.dto.UserResponse;
import com.oma.mecash.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "Create a new customer")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomResponse.class))})
//    })
    @RequestMapping(value = "/account", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<SignUpResponse> createAccount(@Validated @RequestBody CreateUserDTO createUser) {

        SignUpResponse user = userService.createUser(createUser);
        return new APIResponse<>("User registered successfully", 201, user);
    }

    @RequestMapping(value = "/account", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<String> updateAccount(@Validated @RequestBody UpdateUserDTO updateUser) {

        return new APIResponse<>("User registered successfully", 200, userService.updateUser(updateUser));
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<UserResponse> getAccount() {
        log.info("Entered getAccount");
        return new APIResponse<>("Request Successful", 200, userService.getUser());
    }

    @RequestMapping(value = "/generate-access-token", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<AccessTokenResponse> generateAccessToken(@Validated @RequestBody AccessTokenRequest accessTokenRequest) {
        return new APIResponse<>("Token generated", 200, userService.generateAccessToken(accessTokenRequest));
    }

    @RequestMapping(value = "/create-pin", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<String> createPin(@RequestBody String pin) {
        return new APIResponse<>("Request Successful", 201, userService.createPin(pin));
    }

    @RequestMapping(value = "/update-pin", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<String> updatePin(@RequestBody String pin) {
        return new APIResponse<>("Request Successful", 200, userService.updateTransactionPin(pin));
    }
}
