package com.oma.mecash.wallet_service.service;

import com.oma.mecash.security_service.model.SecurityUser;
import com.oma.mecash.security_service.model.entity.AuthUser;
import com.oma.mecash.security_service.service.AuthUserService;
import com.oma.mecash.wallet_service.dto.DepositorDTO;
import com.oma.mecash.wallet_service.dto.TransactionResponse;
import com.oma.mecash.wallet_service.exception.InvalidAmountException;
import com.oma.mecash.wallet_service.model.entity.TransactionData;
import com.oma.mecash.wallet_service.model.entity.Wallet;
import com.oma.mecash.wallet_service.model.enums.Currency;
import com.oma.mecash.wallet_service.model.enums.TransactionType;
import com.oma.mecash.wallet_service.repository.TransactionRepository;
import com.oma.mecash.wallet_service.repository.WalletRepository;
import com.oma.mecash.wallet_service.util.CurrencyExchangeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final AuthUserService authUserService;

    @Value("${institution.number}")
    private String institutionNumber;

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse depositToWallet(DepositorDTO depositorDTO) {
        double amount = depositorDTO.getAmount();
        if (amount <= 0) {
            throw new InvalidAmountException("Amount cannot be less than zero");
        }

        Wallet wallet = walletRepository.findWalletByAccountNumber(depositorDTO.getAccountNumber());

        double exchangeRate = CurrencyExchangeUtil.getDefaultRate(depositorDTO.getCurrency().toString(), wallet.getCurrency().toString());

        double amountReceived = creditWallet(wallet, depositorDTO, exchangeRate);

        Wallet savedWallet = walletRepository.save(wallet);

        TransactionData transactionData = createTransactionData(savedWallet, depositorDTO, amountReceived, amount, amountReceived,
                depositorDTO.getCurrency(), savedWallet.getCurrency(), TransactionType.CREDIT
        );
        TransactionData savedTransactionData = transactionRepository.save(transactionData);

        return buildTransactionResponse(savedTransactionData, amount);
    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse transfer(DepositorDTO depositorDTO) {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());

        double amount = depositorDTO.getAmount();
        if (amount <= 0) {
            throw new InvalidAmountException("Amount cannot be less than zero");
        }

        Wallet depositorWallet = walletRepository.findWalletByUserId(authUser.getUserId());
        Wallet beneficiaryWallet = walletRepository.findWalletByAccountNumber(depositorDTO.getAccountNumber());

        double exchangeRate = CurrencyExchangeUtil.getDefaultRate(depositorWallet.getCurrency().toString(), beneficiaryWallet.getCurrency().toString());

        double amountInBeneficiaryCurrency = debitWallet(depositorWallet, beneficiaryWallet, depositorDTO, exchangeRate);

        Wallet savedDepositorWallet = walletRepository.save(depositorWallet);
        Wallet savedBeneficiaryWallet = walletRepository.save(beneficiaryWallet);

        TransactionData transactionData = createTransactionData(savedDepositorWallet, depositorDTO, amount, amount,
                amountInBeneficiaryCurrency, savedDepositorWallet.getCurrency(), savedBeneficiaryWallet.getCurrency(),
                TransactionType.DEBIT
        );
        TransactionData savedTransactionData = transactionRepository.save(transactionData);

        TransactionData receiverTransactionData = createTransactionData(savedBeneficiaryWallet, depositorDTO,
                amountInBeneficiaryCurrency, amount, amountInBeneficiaryCurrency, savedDepositorWallet.getCurrency(),
                savedBeneficiaryWallet.getCurrency(), TransactionType.CREDIT);
        TransactionData savedReceiverTransactionData = transactionRepository.save(receiverTransactionData);

        return buildTransactionResponse(savedTransactionData, amount);
    }

    public List<TransactionResponse> getTransactionHistory() {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());

        return transactionRepository.findByUserId(authUser.getUserId())
                .stream()
                .map(transactionData -> buildTransactionResponse(transactionData, transactionData.getAmount()))
                .collect(Collectors.toList());
    }

    private double creditWallet(Wallet wallet, DepositorDTO depositorDTO, double exchangeRate) {
        double amountDeposited = exchangeRate * depositorDTO.getAmount();
        double amount = wallet.getAmount();
        double totalAmount = amount + amountDeposited;
        wallet.setAmount(totalAmount);
        return amountDeposited;
    }

    private double debitWallet(Wallet depositorWallet, Wallet beneficiaryWallet, DepositorDTO depositorDTO, double exchangeRate) {
        double amountToTransfer = depositorDTO.getAmount();
        double depositorWalletAmount = depositorWallet.getAmount();
        if (depositorWalletAmount <= amountToTransfer) {
            throw new InvalidAmountException("Not enough credit to do transfer");
        }
        double remainingAmount = depositorWalletAmount - amountToTransfer;
        depositorWallet.setAmount(remainingAmount);

        double receiverWalletAmount = beneficiaryWallet.getAmount();
        double amountToTransferInReceiverCurrency = exchangeRate * amountToTransfer;
        double totalAmountInReceiverWallet = receiverWalletAmount + amountToTransferInReceiverCurrency;
        beneficiaryWallet.setAmount(totalAmountInReceiverWallet);

        return amountToTransferInReceiverCurrency;
    }

    private TransactionData createTransactionData(
            Wallet wallet, DepositorDTO depositorDTO, double amount, double amountInSenderCurrency, double amountInReceiverCurrency,
            Currency senderCurrency, Currency receiverCurrency, TransactionType transactionType
    ) {
        return TransactionData.builder()
                .wallet(wallet)
                .userId(wallet.getUserId())
                .currency(wallet.getCurrency())
                .senderCurrency(senderCurrency)
                .receiverCurrency(receiverCurrency)
                .accountName(wallet.getAccountName())
                .accountNumber(wallet.getAccountNumber())
                .amount(amount)
                .amountInSenderCurrency(amountInSenderCurrency)
                .amountInReceiverCurrency(amountInReceiverCurrency)
                .description(depositorDTO.getDescription())
                .referenceNumber(generateReferenceNumber())
                .transactionType(TransactionType.CREDIT)
                .build();
    }

    private TransactionResponse buildTransactionResponse(TransactionData transactionData, double amount) {
        return TransactionResponse.builder()
                .walletId(transactionData.getWallet().getId().toString())
                .transactionId(transactionData.getId().toString())
                .amount(amount)
                .accountName(transactionData.getAccountName())
                .accountNumber(transactionData.getAccountNumber())
                .referenceNumber(transactionData.getReferenceNumber())
                .currency(transactionData.getCurrency())
                .sender(transactionData.getSender())
                .description(transactionData.getDescription())
                .build();
    }


    private String generateReferenceNumber() {
        String formatLocalDateTime = formatLocalDateTime(LocalDateTime.now(), "yyMMddHHmmss");
        String random12Digits = generateRandomDigits(12);
        return institutionNumber + formatLocalDateTime + random12Digits;
    }

    private String generateRandomDigits(int digits) {
        if (digits < 1 || digits > 18) { // Limiting to prevent overflow
            throw new IllegalArgumentException("Digits must be between 1 and 18");
        }

        Random random = new Random();
        long min = (long) Math.pow(10, digits - 1);
        long max = (long) Math.pow(10, digits) - 1;

        return String.valueOf(min + (long) (random.nextDouble() * (max - min)));
    }

    private String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }
}
