package com.oma.mecash.wallet_service.service;

import com.oma.mecash.security_service.model.SecurityUser;
import com.oma.mecash.security_service.model.entity.AuthUser;
import com.oma.mecash.security_service.service.AuthUserService;
import com.oma.mecash.wallet_service.dto.CreateWalletRequest;
import com.oma.mecash.wallet_service.dto.CreateWalletResponse;
import com.oma.mecash.wallet_service.dto.WalletResponse;
import com.oma.mecash.wallet_service.model.entity.Wallet;
import com.oma.mecash.wallet_service.model.enums.AccountType;
import com.oma.mecash.wallet_service.repository.WalletRepository;
import com.oma.mecash.wallet_service.service.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final AuthUserService authUserService;
    private final WalletRepository walletRepository;

    public CreateWalletResponse createWallet(CreateWalletRequest request) {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());

        Wallet wallet = Wallet.builder()
                .accountType(AccountType.CUSTOMER)
                .userId(authUser.getUserId())
                .currency(request.getCurrency())
                .country(request.getCountry())
                .accountName(request.getAccountName())
                .accountNumber(request.getAccountNumber())
                .bankName(request.getBankName())
                .build();

        Wallet savedWallet = walletRepository.save(wallet);
        return new WalletMapper().walletToCreateWalletResponse(savedWallet);
    }

    public WalletResponse getWallet() {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());

        Wallet wallet = walletRepository.findWalletByUserId(authUser.getUserId());
        return new WalletMapper().walletToWalletResponse(wallet);

    }


}
