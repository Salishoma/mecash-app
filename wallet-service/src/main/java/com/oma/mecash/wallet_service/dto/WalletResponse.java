package com.oma.mecash.wallet_service.dto;

import com.oma.mecash.wallet_service.model.enums.Currency;
import lombok.Builder;

@Builder
public class WalletResponse {

    private String WalletId;

    private String userId;

    private Currency currency;

    private double amount;

    private String country = "NG";

}
