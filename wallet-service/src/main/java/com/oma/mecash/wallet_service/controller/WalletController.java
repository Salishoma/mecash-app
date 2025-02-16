package com.oma.mecash.wallet_service.controller;

import com.oma.mecash.wallet_service.dto.APIResponse;
import com.oma.mecash.wallet_service.dto.CreateWalletRequest;
import com.oma.mecash.wallet_service.dto.CreateWalletResponse;
import com.oma.mecash.wallet_service.dto.WalletResponse;
import com.oma.mecash.wallet_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

//    @Operation(summary = "To create wallet")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomResponse.class))})
//    })
    @RequestMapping(value = "/account", method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<CreateWalletResponse> createWallet(@RequestBody CreateWalletRequest request){
        return new APIResponse<>("wallet successfully created", 201,
                walletService.createWallet(request));
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<WalletResponse> getWallet(){
        return new APIResponse<>("wallet successfully created", 200,
                walletService.getWallet());
    }

}
