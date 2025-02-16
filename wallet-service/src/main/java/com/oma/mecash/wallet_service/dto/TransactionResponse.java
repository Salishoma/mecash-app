package com.oma.mecash.wallet_service.dto;

import com.oma.mecash.wallet_service.model.enums.Currency;
import lombok.Builder;

@Builder
public class TransactionResponse {

    private String transactionId;

    private String walletId;

    private Currency currency;

    private double amount;

    private String sender;

    private String accountName;

    private String accountNumber;

    private String description;

    private String referenceNumber;

}
