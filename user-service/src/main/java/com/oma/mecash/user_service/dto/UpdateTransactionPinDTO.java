package com.oma.mecash.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTransactionPinDTO {
    String newPin;
    String oldPin;
}
