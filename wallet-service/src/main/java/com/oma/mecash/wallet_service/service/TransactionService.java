package com.oma.mecash.wallet_service.service;

import com.oma.mecash.security_service.exception.TransactionPinException;
import com.oma.mecash.security_service.model.SecurityUser;
import com.oma.mecash.security_service.model.entity.AuthUser;
import com.oma.mecash.security_service.service.AuthUserService;
import com.oma.mecash.wallet_service.dto.DepositorDTO;
import com.oma.mecash.wallet_service.dto.TransactionResponse;
import com.oma.mecash.wallet_service.exception.InvalidAmountException;
import com.oma.mecash.wallet_service.exception.WalletNotFoundException;
import com.oma.mecash.wallet_service.model.entity.TransactionData;
import com.oma.mecash.wallet_service.model.entity.Wallet;
import com.oma.mecash.wallet_service.model.enums.Currency;
import com.oma.mecash.wallet_service.model.enums.TransactionType;
import com.oma.mecash.wallet_service.repository.TransactionRepository;
import com.oma.mecash.wallet_service.repository.WalletRepository;
import com.oma.mecash.wallet_service.util.CurrencyExchangeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    private final PasswordEncoder passwordEncoder;

    @Value("${institution.number}")
    private String institutionNumber;

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse depositToWallet(DepositorDTO depositorDTO) {
        BigDecimal amount = depositorDTO.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount cannot be less than zero");
        }

        Wallet wallet = walletRepository.findWalletByAccountNumber(depositorDTO.getBeneficiaryAccountNumber())
                .orElseThrow(() -> new WalletNotFoundException("The beneficiary does not have a wallet"));

        BigDecimal exchangeRate = CurrencyExchangeUtil.getDefaultRate(depositorDTO.getCurrency().toString(), wallet.getCurrency().toString());

        BigDecimal amountReceived = creditWallet(wallet, depositorDTO, exchangeRate);

        Wallet savedWallet = walletRepository.save(wallet);

        TransactionData transactionData = createTransactionData(savedWallet, depositorDTO, amountReceived, amount, amountReceived,
                depositorDTO.getCurrency(), savedWallet.getCurrency(), TransactionType.CREDIT
        );
        transactionData.setCreatedOn(LocalDateTime.now());
        transactionData.setLastModifiedOn(LocalDateTime.now());
        transactionData.setCreatedBy(wallet.getUserId().toString());
        transactionData.setLastModifiedBy(wallet.getUserId().toString());

        TransactionData savedTransactionData = transactionRepository.save(transactionData);

        return buildTransactionResponse(savedTransactionData, amount);
    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse transfer(DepositorDTO depositorDTO) {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());
        String transactionPin = authUser.getTransactionPin();
        System.out.println("transactionPin: " + transactionPin);

        if(!passwordEncoder.matches(depositorDTO.getTransactionPin(), transactionPin)){
            throw new TransactionPinException("Invalid pin. Transaction terminated");
        }

        BigDecimal amount = depositorDTO.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount cannot be less than zero");
        }

        Wallet depositorWallet = walletRepository.findWalletByUserId(authUser.getUserId())
                .orElseThrow(() -> new WalletNotFoundException("You need to create a wallet to make transactions"));
        Wallet beneficiaryWallet = walletRepository.findWalletByAccountNumber(depositorDTO.getBeneficiaryAccountNumber())
                .orElseThrow(() -> new WalletNotFoundException("The beneficiary does not have a wallet"));

        BigDecimal exchangeRate = CurrencyExchangeUtil.getDefaultRate(depositorWallet.getCurrency().toString(), beneficiaryWallet.getCurrency().toString());

        BigDecimal amountInBeneficiaryCurrency = debitWallet(depositorWallet, beneficiaryWallet, depositorDTO, exchangeRate);

        Wallet savedDepositorWallet = walletRepository.save(depositorWallet);
        Wallet savedBeneficiaryWallet = walletRepository.save(beneficiaryWallet);

        TransactionData transactionData = createTransactionData(savedDepositorWallet, depositorDTO, amount, amount,
                amountInBeneficiaryCurrency, savedDepositorWallet.getCurrency(), savedBeneficiaryWallet.getCurrency(),
                TransactionType.DEBIT
        );

        transactionData.setCreatedOn(LocalDateTime.now());
        transactionData.setLastModifiedOn(LocalDateTime.now());
        transactionData.setCreatedBy(savedDepositorWallet.getUserId().toString());
        transactionData.setLastModifiedBy(savedDepositorWallet.getUserId().toString());

        TransactionData savedTransactionData = transactionRepository.save(transactionData);

        TransactionData receiverTransactionData = createTransactionData(savedBeneficiaryWallet, depositorDTO,
                amountInBeneficiaryCurrency, amount, amountInBeneficiaryCurrency, savedDepositorWallet.getCurrency(),
                savedBeneficiaryWallet.getCurrency(), TransactionType.CREDIT);
        TransactionData savedReceiverTransactionData = transactionRepository.save(receiverTransactionData);

        transactionData.setCreatedOn(LocalDateTime.now());
        transactionData.setLastModifiedOn(LocalDateTime.now());
        transactionData.setCreatedBy(savedReceiverTransactionData.getUserId().toString());
        transactionData.setLastModifiedBy(savedReceiverTransactionData.getUserId().toString());

        return buildTransactionResponse(savedTransactionData, amount);
    }

    public Page<TransactionResponse> getTransactionHistory(Pageable pageable) {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());

        return transactionRepository.findByUserId(authUser.getUserId(), pageable)
                .map(transactionData -> buildTransactionResponse(transactionData, transactionData.getAmount()));
    }

    private BigDecimal creditWallet(Wallet wallet, DepositorDTO depositorDTO, BigDecimal exchangeRate) {
        BigDecimal amountDeposited = depositorDTO.getAmount().multiply(exchangeRate);
        BigDecimal amount = wallet.getAmount();
        BigDecimal totalAmount = amount.add(amountDeposited);
        wallet.setAmount(totalAmount);
        return amountDeposited;
    }

    private BigDecimal debitWallet(Wallet depositorWallet, Wallet beneficiaryWallet, DepositorDTO depositorDTO, BigDecimal exchangeRate) {
        BigDecimal amountToTransfer = depositorDTO.getAmount();
        BigDecimal depositorWalletAmount = depositorWallet.getAmount();
        if ((depositorWalletAmount.compareTo(amountToTransfer)) <= 0) {
            throw new InvalidAmountException("Not enough credit to do transfer");
        }
        BigDecimal remainingAmount = depositorWalletAmount.subtract(amountToTransfer);
        depositorWallet.setAmount(remainingAmount);

        BigDecimal receiverWalletAmount = beneficiaryWallet.getAmount();
        BigDecimal amountToTransferInReceiverCurrency = amountToTransfer.multiply(exchangeRate);
        BigDecimal totalAmountInReceiverWallet = receiverWalletAmount.add(amountToTransferInReceiverCurrency);
        beneficiaryWallet.setAmount(totalAmountInReceiverWallet);

        return amountToTransferInReceiverCurrency;
    }

    private TransactionData createTransactionData(
            Wallet wallet, DepositorDTO depositorDTO, BigDecimal amount, BigDecimal amountInSenderCurrency, BigDecimal amountInReceiverCurrency,
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
                .transactionType(transactionType)
                .build();
    }

    private TransactionResponse buildTransactionResponse(TransactionData transactionData, BigDecimal amount) {
        return TransactionResponse.builder()
                .walletId(transactionData.getWallet().getId().toString())
                .transactionId(transactionData.getId().toString())
                .amount(amount)
                .accountName(transactionData.getAccountName())
                .accountNumber(transactionData.getAccountNumber())
                .referenceNumber(transactionData.getReferenceNumber())
                .currency(transactionData.getCurrency())
                .sender(transactionData.getSender())
                .transactionType(transactionData.getTransactionType())
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
