package com.oma.mecash.user_service.controller;

import com.oma.mecash.security_service.model.SignUpResponse;
import com.oma.mecash.user_service.dto.APIResponse;
import com.oma.mecash.user_service.dto.AccessTokenRequest;
import com.oma.mecash.user_service.dto.AccessTokenResponse;
import com.oma.mecash.user_service.dto.CreateUserDTO;
import com.oma.mecash.user_service.dto.UpdateUserDTO;
import com.oma.mecash.user_service.dto.UserResponse;
import com.oma.mecash.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a New Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))})
    })
    @RequestMapping(value = "/account", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<SignUpResponse> createAccount(@Validated @RequestBody CreateUserDTO createUser) {
        SignUpResponse user = userService.createUser(createUser);
        return new APIResponse<>("User registered successfully", HttpStatus.CREATED, user);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an Existing Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))})
    })
    @RequestMapping(value = "/account", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<String> updateAccount(@Validated @RequestBody UpdateUserDTO updateUser) {
        return new APIResponse<>("User registered successfully", HttpStatus.OK, userService.updateUser(updateUser));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get User's Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))})
    })
    @RequestMapping(value = "/account", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<UserResponse> getAccount() {
        return new APIResponse<>("Request Successful", HttpStatus.OK, userService.getUser());
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Generate Access Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))})
    })
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<AccessTokenResponse> generateAccessToken(@Validated @RequestBody AccessTokenRequest accessTokenRequest) {
        return new APIResponse<>("Token generated", HttpStatus.OK, userService.generateAccessToken(accessTokenRequest));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Transaction Pin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))})
    })
    @RequestMapping(value = "/create-pin", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<String> createPin(@RequestBody String pin) {
        return new APIResponse<>("Request Successful", HttpStatus.CREATED, userService.createPin(pin));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Transaction Pin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))})
    })
    @RequestMapping(value = "/update-pin", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<String> updatePin(@RequestBody String pin) {
        return new APIResponse<>("Request Successful", HttpStatus.OK, userService.updateTransactionPin(pin));
    }
}
