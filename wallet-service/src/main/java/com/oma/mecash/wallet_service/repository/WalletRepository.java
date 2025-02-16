package com.oma.mecash.wallet_service.repository;

import com.oma.mecash.wallet_service.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Wallet findWalletByUserId(UUID id);
    Wallet findWalletByAccountNumber(String accountNumber);

    @Procedure(name = "initiate_transfer")
    void initiateTransfer(int senderId, int receiverId, double amount);
}
