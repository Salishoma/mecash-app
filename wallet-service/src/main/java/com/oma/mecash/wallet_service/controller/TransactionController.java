package com.oma.mecash.wallet_service.controller;

import com.oma.mecash.wallet_service.dto.APIResponse;
import com.oma.mecash.wallet_service.dto.DepositorDTO;
import com.oma.mecash.wallet_service.dto.TransactionResponse;
import com.oma.mecash.wallet_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @RequestMapping(value = "/deposit", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<TransactionResponse> depositToWallet(@Valid @RequestBody DepositorDTO depositorDTO){
        return new APIResponse<>("Transaction successful", 200,
                transactionService.depositToWallet(depositorDTO));
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<TransactionResponse> transfer(@Valid @RequestBody DepositorDTO depositorDTO){
        return new APIResponse<>("Transaction successful", 200,
                transactionService.transfer(depositorDTO));
    }

    @RequestMapping(value = "/transaction-history", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<List<TransactionResponse>> getTransactionHistory(){
        return new APIResponse<>("Transaction successful", 200,
                transactionService.getTransactionHistory());
    }
}
