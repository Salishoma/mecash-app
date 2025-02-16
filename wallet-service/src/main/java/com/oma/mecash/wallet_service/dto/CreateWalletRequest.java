package com.oma.mecash.wallet_service.dto;

import com.oma.mecash.wallet_service.model.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWalletRequest {

    private Currency currency = Currency.NGN;
    private String country = "NG";

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Account number is required")
    @Size(min = 10, max = 11, message = "Invalid account number")
    private String accountNumber;

    @NotBlank(message = "Bank name is required")
    private String bankName;
}
