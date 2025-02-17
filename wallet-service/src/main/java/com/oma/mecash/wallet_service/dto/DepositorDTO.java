package com.oma.mecash.wallet_service.dto;

import com.oma.mecash.wallet_service.model.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Builder
@Setter
public class DepositorDTO {

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @Positive(message = "Amount should be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Beneficiary account name is required")
    private String beneficiaryAccountName;

    @NotBlank(message = "Beneficiary account number is required")
    private String beneficiaryAccountNumber;

    @NotBlank(message = "Destination bank name is required")
    private String destinationBank;

    private String description;

    private Currency currency;

    private String transactionPin;

}
