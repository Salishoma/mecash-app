package com.oma.mecash.wallet_service.dto;

import com.oma.mecash.wallet_service.model.enums.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class WalletResponse {

    private String WalletId;

    private String userId;

    private Currency currency;

    private BigDecimal amount;

    private String country = "NG";

}
