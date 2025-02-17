package com.oma.mecash.wallet_service.exception;

import com.oma.mecash.security_service.exception.AuthenticateUserException;
import com.oma.mecash.security_service.exception.TransactionPinException;
import com.oma.mecash.wallet_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {


    @ExceptionHandler(value = InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponse<?>> handleUserInvalidAmountException(InvalidAmountException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("Bad Request: " + exception.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = TransactionPinException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponse<?>> handleTransactionPinException(TransactionPinException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("Bad Request: " + exception.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse<?>> handleWalletNotFoundException(WalletNotFoundException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("Not Found: " + exception.getMessage()).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = WalletExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponse<?>> handleAuthenticateUserException(WalletExistsException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("Bad Request: " + exception.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = AuthenticateUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ResponseEntity<ErrorResponse<?>> handleAuthenticateUserException(AuthenticateUserException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("Unauthorized User: " + exception.getMessage()).status(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
