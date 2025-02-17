package com.oma.mecash.wallet_service.service;

import com.oma.mecash.security_service.model.SecurityUser;
import com.oma.mecash.security_service.model.entity.AuthUser;
import com.oma.mecash.security_service.service.AuthUserService;
import com.oma.mecash.wallet_service.dto.CreateWalletRequest;
import com.oma.mecash.wallet_service.dto.CreateWalletResponse;
import com.oma.mecash.wallet_service.dto.WalletResponse;
import com.oma.mecash.wallet_service.exception.WalletExistsException;
import com.oma.mecash.wallet_service.exception.WalletNotFoundException;
import com.oma.mecash.wallet_service.model.entity.Wallet;
import com.oma.mecash.wallet_service.model.enums.AccountType;
import com.oma.mecash.wallet_service.repository.WalletRepository;
import com.oma.mecash.wallet_service.service.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final AuthUserService authUserService;
    private final WalletRepository walletRepository;

    public CreateWalletResponse createWallet(CreateWalletRequest request) {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());
        Optional<Wallet> optionalWallet = walletRepository.findWalletByUserId(authUser.getUserId());
        if (optionalWallet.isPresent()) {
            throw new WalletExistsException("You already have a wallet");
        }

        Wallet wallet = Wallet.builder()
                .accountType(AccountType.CUSTOMER)
                .userId(authUser.getUserId())
                .currency(request.getCurrency())
                .country(request.getCountry())
                .accountName(request.getAccountName())
                .accountNumber(request.getAccountNumber())
                .bankName(request.getBankName())
                .build();

        wallet.setCreatedOn(LocalDateTime.now());
        wallet.setLastModifiedOn(LocalDateTime.now());
        wallet.setCreatedBy(wallet.getUserId().toString());
        wallet.setLastModifiedBy(wallet.getUserId().toString());

        Wallet savedWallet = walletRepository.save(wallet);
        return new WalletMapper().walletToCreateWalletResponse(savedWallet);
    }

    public WalletResponse getWallet() {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());

        Wallet wallet = walletRepository.findWalletByUserId(authUser.getUserId())
                .orElseThrow(() -> new WalletNotFoundException("You do not have a wallet"));
        return new WalletMapper().walletToWalletResponse(wallet);

    }


}
