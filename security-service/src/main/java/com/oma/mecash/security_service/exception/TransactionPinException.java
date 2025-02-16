package com.oma.mecash.security_service.exception;

public class TransactionPinException extends RuntimeException {
    public TransactionPinException(String message) {
        super(message);
    }
}
