package com.oma.mecash.wallet_service.exception;

public class WalletExistsException extends RuntimeException {
    public WalletExistsException(String message) {
        super(message);
    }
}
