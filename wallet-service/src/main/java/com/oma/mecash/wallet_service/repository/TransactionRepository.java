package com.oma.mecash.wallet_service.repository;

import com.oma.mecash.wallet_service.model.entity.TransactionData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionData, UUID> {

    Page<TransactionData> findByUserId(UUID userId, Pageable pageable);

}
