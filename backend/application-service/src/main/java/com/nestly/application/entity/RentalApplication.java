package com.nestly.application.entity;

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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rental_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID listingId;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(length = 2000)
    private String message;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "application_documents", joinColumns = @JoinColumn(name = "application_id"))
    @Builder.Default
    private List<String> documentKeys = new ArrayList<>();

    @Column(nullable = false)
    private String status;

    @Column(length = 2000)
    private String ownerResponse;

    private Instant createdAt;

    private Instant updatedAt;
}
