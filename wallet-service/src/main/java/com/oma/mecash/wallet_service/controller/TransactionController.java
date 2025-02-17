package com.oma.mecash.wallet_service.controller;

import com.oma.mecash.wallet_service.dto.APIResponse;
import com.oma.mecash.wallet_service.dto.DepositorDTO;
import com.oma.mecash.wallet_service.dto.ErrorResponse;
import com.oma.mecash.wallet_service.dto.TransactionResponse;
import com.oma.mecash.wallet_service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    /*Just for testing purpose to credit wallet. This shouldn't be used in production*/
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deposit to Wallet Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @RequestMapping(value = "/deposit", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<TransactionResponse> depositToWallet(@Valid @RequestBody DepositorDTO depositorDTO){
        return new APIResponse<>("Transaction successful", HttpStatus.OK,
                transactionService.depositToWallet(depositorDTO));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Transfer to Another Wallet Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @RequestMapping(value = "/transfer", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<TransactionResponse> transfer(@Valid @RequestBody DepositorDTO depositorDTO){
        return new APIResponse<>("Transaction successful", HttpStatus.OK,
                transactionService.transfer(depositorDTO));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Transaction History")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "401", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @RequestMapping(value = "/transaction-history", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<Page<TransactionResponse>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "createdOn,desc") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        Page<TransactionResponse> transactions = transactionService.getTransactionHistory(pageable);

        return new APIResponse<>("Transaction successful", HttpStatus.OK, transactions);
    }
//    public APIResponse<List<TransactionResponse>> getTransactionHistory(){
//        return new APIResponse<>("Transaction successful", HttpStatus.OK,
//                transactionService.getTransactionHistory());
//    }
}
