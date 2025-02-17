package com.oma.mecash.user_service.exception;

import com.oma.mecash.security_service.exception.AuthenticateUserException;
import com.oma.mecash.security_service.exception.TransactionPinException;
import com.oma.mecash.user_service.dto.APIResponse;
import com.oma.mecash.user_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = UserExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponse<?>> handleUserExistsException(UserExistsException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("User exist: " + exception.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse<?>> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("User Not Found: " + exception.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TransactionPinException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponse<?>> handleTransactionPinException(TransactionPinException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("Bad Request: " + exception.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = AuthenticateUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ResponseEntity<ErrorResponse<?>> handleAuthenticateUserException(AuthenticateUserException exception) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("Unauthorized User: " + exception.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse<?> response = ErrorResponse.builder()
                .message("Access Denied: " + ex.getMessage()).status(HttpStatus.FORBIDDEN).build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
