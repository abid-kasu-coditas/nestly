package com.nestly.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nestly.application.client.ListingClient;
import com.nestly.application.client.NotificationClient;
import com.nestly.application.dto.ApplicationResponse;
import com.nestly.application.dto.CreateApplicationRequest;
import com.nestly.application.dto.ListingDto;
import com.nestly.application.dto.NotifyRequest;
import com.nestly.application.dto.RespondRequest;
import com.nestly.application.entity.ApplicationStatus;
import com.nestly.application.entity.RentalApplication;
import com.nestly.application.repository.RentalApplicationRepository;
import com.nestly.shared.util.S3PresignHelper;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private RentalApplicationRepository applicationRepository;
    @Mock
    private ListingClient listingClient;
    @Mock
    private NotificationClient notificationClient;

    private ApplicationService applicationService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID ownerId = UUID.randomUUID();
    private final UUID listingId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(applicationRepository, listingClient,
                notificationClient,
                new S3PresignHelper("ap-south-1", "test", "test", "nestly-docs", null));
    }

    private ListingDto listing(String status) {
        return ListingDto.builder()
                .id(listingId.toString())
                .ownerId(ownerId.toString())
                .title("2BHK in Andheri")
                .status(status)
                .build();
    }

    @Test
    void cannotApplyToAListingThatIsNotLive() {
        when(listingClient.getListing(listingId.toString()))
                .thenReturn(listing("PENDING_VERIFICATION"));

        assertThatThrownBy(() -> applicationService.apply(tenantId.toString(), "TENANT",
                CreateApplicationRequest.builder().listingId(listingId.toString()).build()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("live");
    }

    @Test
    void applyingNotifiesListingOwner() {
        when(listingClient.getListing(listingId.toString())).thenReturn(listing("APPROVED"));
        when(applicationRepository.existsByListingIdAndTenantId(listingId, tenantId)).thenReturn(false);
        when(applicationRepository.save(any())).thenAnswer(inv -> {
            RentalApplication application = inv.getArgument(0);
            application.setId(UUID.randomUUID());
            return application;
        });

        ApplicationResponse response = applicationService.apply(tenantId.toString(), "TENANT",
                CreateApplicationRequest.builder().listingId(listingId.toString())
                        .message("I would love to rent this").build());

        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.PENDING);
        verify(notificationClient).notify(any(NotifyRequest.class));
    }

    @Test
    void onlyTheListingOwnerCanRespond() {
        RentalApplication application = RentalApplication.builder()
                .id(UUID.randomUUID()).listingId(listingId).tenantId(tenantId)
                .status(ApplicationStatus.PENDING).createdAt(Instant.now())
                .build();
        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));
        when(listingClient.getListing(listingId.toString())).thenReturn(listing("APPROVED"));

        String someoneElse = UUID.randomUUID().toString();
        assertThatThrownBy(() -> applicationService.respond(application.getId().toString(),
                someoneElse, RespondRequest.builder().decision("ACCEPTED").build()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("listing owner");
    }

    @Test
    void ownerResponseNotifiesTenant() {
        RentalApplication application = RentalApplication.builder()
                .id(UUID.randomUUID()).listingId(listingId).tenantId(tenantId)
                .status(ApplicationStatus.PENDING).createdAt(Instant.now())
                .build();
        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));
        when(listingClient.getListing(listingId.toString())).thenReturn(listing("APPROVED"));
        when(applicationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ApplicationResponse response = applicationService.respond(application.getId().toString(),
                ownerId.toString(),
                RespondRequest.builder().decision("ACCEPTED").message("Welcome!").build());

        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
        verify(notificationClient).notify(any(NotifyRequest.class));
    }
}
