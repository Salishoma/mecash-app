package com.oma.mecash.wallet_service.repository;

import com.oma.mecash.wallet_service.model.entity.TransactionData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionData, UUID> {

    List<TransactionData> findByUserId(UUID userId);

}
