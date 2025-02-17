package com.oma.mecash.wallet_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Setter
@Getter
public class APIResponse<T> {
    private String message;
    private HttpStatus status;
    private T data;


}
