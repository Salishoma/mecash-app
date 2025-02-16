package com.oma.mecash.wallet_service.dto;

import com.oma.mecash.wallet_service.model.enums.Currency;
import lombok.Builder;

@Builder
public class CreateWalletResponse {
    String walletId;

    Currency currency;
    double amount;
}
