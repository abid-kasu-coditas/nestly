package com.nestly.listing.repository;

import com.nestly.listing.entity.Listing;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, UUID> {

    List<Listing> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId);

    List<Listing> findByStatusOrderByCreatedAtDesc(String status);
}
