package com.oma.mecash.wallet_service.service.mapper;

import com.oma.mecash.wallet_service.dto.CreateWalletResponse;
import com.oma.mecash.wallet_service.dto.WalletResponse;
import com.oma.mecash.wallet_service.model.entity.Wallet;

public class WalletMapper {

    public CreateWalletResponse walletToCreateWalletResponse(Wallet wallet) {
        return CreateWalletResponse.builder().walletId(wallet.getId().toString())
                .amount(wallet.getAmount())
                .currency(wallet.getCurrency())
                .build();
    }

    public WalletResponse walletToWalletResponse(Wallet wallet) {
        return WalletResponse.builder()
                .WalletId(wallet.getId().toString())
                .amount(wallet.getAmount())
                .currency(wallet.getCurrency())
                .userId(wallet.getUserId().toString())
                .country(wallet.getCountry())
                .build();
    }
}
