package com.nestly.auth.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String role;
    private String displayName;
    private String contact;
    private String profilePictureUrl;
    private String verificationDocUrl;
    private Instant createdAt;
}
