package com.nestly.application.dto;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private String id;
    private String listingId;
    private String tenantId;
    private String message;
    private String status;
    private String ownerResponse;
    private List<String> documentUrls;
    private Instant createdAt;
    private Instant updatedAt;
}
