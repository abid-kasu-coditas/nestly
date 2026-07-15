package com.nestly.listing.dto;

import java.math.BigDecimal;
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
public class ListingResponse {
    private String id;
    private String ownerId;
    private String title;
    private String description;
    private String location;
    private BigDecimal rent;
    private int rooms;
    private List<String> amenities;
    private List<String> photoUrls;
    private List<String> documentUrls;
    private String status;
    private String adminComment;
    private Instant createdAt;
    private Instant updatedAt;
}
