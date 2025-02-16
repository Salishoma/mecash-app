package com.oma.mecash.user_service.exception;

import com.oma.mecash.security_service.exception.AuthenticateUserException;
import com.oma.mecash.security_service.exception.TransactionPinException;
import com.oma.mecash.user_service.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = UserExistsException.class)
    public final APIResponse<String> handleUserExistsException(UserExistsException exception) {
        return new APIResponse<>(exception.getMessage(), HttpStatus.NOT_FOUND.value(), "This account has been assigned");
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public final APIResponse<String> handleUserNotFoundException(UserNotFoundException exception) {
        return new APIResponse<>(exception.getMessage(), HttpStatus.NOT_FOUND.value(), "User Not Found");
    }

    @ExceptionHandler(value = TransactionPinException.class)
    public final APIResponse<String> handleTransactionPinException(TransactionPinException exception) {
        return new APIResponse<>(exception.getMessage(), HttpStatus.NOT_FOUND.value(), "User Not Found");
    }

    @ExceptionHandler(value = AuthenticateUserException.class)
    public final ResponseEntity<String> handleAuthenticateUserException(AuthenticateUserException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
