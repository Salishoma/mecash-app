//package com.oma.mecash.security_service.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class SecurityExceptionControllerAdvice {
//
//    @ExceptionHandler(value = AuthenticateUserException.class)
//    public final ResponseEntity<String> handleAuthenticateUserException(AuthenticateUserException exception) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
//
//}
