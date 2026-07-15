package com.nestly.application.repository;

import com.nestly.application.entity.RentalApplication;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalApplicationRepository extends JpaRepository<RentalApplication, UUID> {

    List<RentalApplication> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);

    List<RentalApplication> findByListingIdOrderByCreatedAtDesc(UUID listingId);

    boolean existsByListingIdAndTenantId(UUID listingId, UUID tenantId);
}
