package com.oma.mecash.wallet_service.dto;

import com.oma.mecash.wallet_service.model.enums.Currency;
import com.oma.mecash.wallet_service.model.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class TransactionResponse {

    private String transactionId;

    private String walletId;

    private Currency currency;

    private BigDecimal amount;

    private String sender;

    private String accountName;

    private String accountNumber;

    private String description;

    private String referenceNumber;

    private TransactionType transactionType;

}
