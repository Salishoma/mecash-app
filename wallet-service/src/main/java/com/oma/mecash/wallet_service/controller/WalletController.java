package com.oma.mecash.wallet_service.controller;

import com.oma.mecash.wallet_service.dto.APIResponse;
import com.oma.mecash.wallet_service.dto.CreateWalletRequest;
import com.oma.mecash.wallet_service.dto.CreateWalletResponse;
import com.oma.mecash.wallet_service.dto.ErrorResponse;
import com.oma.mecash.wallet_service.dto.WalletResponse;
import com.oma.mecash.wallet_service.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Wallet Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "401", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @RequestMapping(value = "/account", method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<CreateWalletResponse> createWallet(@RequestBody CreateWalletRequest request){
        return new APIResponse<>("wallet successfully created", HttpStatus.CREATED,
                walletService.createWallet(request));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "401", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @RequestMapping(value = "/account", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<WalletResponse> getWallet(){
        return new APIResponse<>("wallet successfully created", HttpStatus.OK,
                walletService.getWallet());
    }

}
