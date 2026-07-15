package com.nestly.listing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateListingRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String location;

    @NotNull
    @Positive
    private BigDecimal rent;

    @Positive
    private int rooms;

    private List<String> amenities;
}
