package com.nestly.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpEmailRequest {
    private String email;
    private String otp;
    private int expiryMinutes;
}
