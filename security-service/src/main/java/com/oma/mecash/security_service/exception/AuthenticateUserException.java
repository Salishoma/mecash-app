package com.oma.mecash.security_service.exception;

public class AuthenticateUserException extends RuntimeException {
    public AuthenticateUserException(String message) {
        super(message);
    }
}
