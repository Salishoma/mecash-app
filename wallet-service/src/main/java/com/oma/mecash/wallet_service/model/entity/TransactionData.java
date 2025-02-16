package com.oma.mecash.wallet_service.model.entity;

import com.oma.mecash.wallet_service.model.BaseEntity;
import com.oma.mecash.wallet_service.model.enums.Currency;
import com.oma.mecash.wallet_service.model.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "transaction_data")
public class TransactionData extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Currency senderCurrency;

    @Enumerated(EnumType.STRING)
    private Currency receiverCurrency;

    @Column(name = "amount")
    private double amount;

    @Column(name = "amount_in_sender_currency")
    private double amountInSenderCurrency;

    @Column(name = "amount_in_receiver_currency")
    private double amountInReceiverCurrency;

    @Column(name = "sender")
    private String sender;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "user_id")
    private UUID userId;
}
