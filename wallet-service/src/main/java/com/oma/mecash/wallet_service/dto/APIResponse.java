package com.oma.mecash.wallet_service.dto;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class APIResponse<T> {
    private String message;
    private HttpStatus status;
    private T data;


}
