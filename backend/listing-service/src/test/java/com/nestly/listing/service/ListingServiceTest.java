package com.nestly.listing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nestly.listing.client.NotificationClient;
import com.nestly.listing.dto.CreateListingRequest;
import com.nestly.listing.dto.ListingResponse;
import com.nestly.listing.entity.Listing;
import com.nestly.listing.entity.ListingStatus;
import com.nestly.listing.repository.ListingRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;
    @Mock
    private NotificationClient notificationClient;

    private ListingService listingService;

    private final UUID ownerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        listingService = new ListingService(listingRepository, notificationClient,
                new com.nestly.shared.util.S3PresignHelper("ap-south-1", "test", "test", "nestly-docs", null));
    }

    private CreateListingRequest request() {
        return CreateListingRequest.builder()
                .title("2BHK in Andheri")
                .description("Bright 2 bedroom flat close to the metro station.")
                .location("Mumbai")
                .rent(new BigDecimal("30000"))
                .rooms(2)
                .amenities(List.of("parking"))
                .build();
    }

    @Test
    void createStartsInPendingVerificationForManualAdminReview() {
        when(listingRepository.save(any())).thenAnswer(inv -> {
            Listing listing = inv.getArgument(0);
            if (listing.getId() == null) {
                listing.setId(UUID.randomUUID());
            }
            return listing;
        });

        ListingResponse response = listingService.create(ownerId.toString(), "OWNER", request());

        assertThat(response.getStatus()).isEqualTo(ListingStatus.PENDING_VERIFICATION);
        verify(notificationClient, never()).notify(any());
    }

    @Test
    void onlyOwnersCanCreateListings() {
        assertThatThrownBy(() -> listingService.create(ownerId.toString(), "TENANT", request()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Only owners");
    }

    @Test
    void adminApprovalSetsStatusAndNotifiesOwner() {
        Listing listing = Listing.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .title("2BHK in Andheri")
                .status(ListingStatus.PENDING_VERIFICATION)
                .createdAt(Instant.now())
                .build();
        when(listingRepository.findById(listing.getId())).thenReturn(Optional.of(listing));
        when(listingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ListingResponse response = listingService.decide(
                listing.getId().toString(), "ADMIN", ListingStatus.APPROVED, "Looks good");

        assertThat(response.getStatus()).isEqualTo(ListingStatus.APPROVED);
        verify(notificationClient).notify(any());
    }

    @Test
    void nonAdminCannotDecide() {
        assertThatThrownBy(() -> listingService.decide(
                UUID.randomUUID().toString(), "OWNER", ListingStatus.APPROVED, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Only admins");
    }
}
