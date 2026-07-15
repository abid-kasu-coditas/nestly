package com.nestly.listing.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "listings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;

    private String location;

    private BigDecimal rent;

    private int rooms;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "listing_amenities", joinColumns = @JoinColumn(name = "listing_id"))
    @Builder.Default
    private List<String> amenities = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "listing_photos", joinColumns = @JoinColumn(name = "listing_id"))
    @Builder.Default
    private List<String> photoKeys = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "listing_documents", joinColumns = @JoinColumn(name = "listing_id"))
    @Builder.Default
    private List<String> documentKeys = new ArrayList<>();

    @Column(nullable = false)
    private String status;

    @Column(length = 2000)
    private String adminComment;

    private Instant createdAt;

    private Instant updatedAt;
}
