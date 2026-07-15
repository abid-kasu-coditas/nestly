package com.nestly.application.service;

import com.nestly.application.client.ListingClient;
import com.nestly.application.client.NotificationClient;
import com.nestly.application.dto.ApplicationResponse;
import com.nestly.application.dto.CreateApplicationRequest;
import com.nestly.application.dto.ListingDto;
import com.nestly.application.dto.NotifyRequest;
import com.nestly.application.dto.PresignResponse;
import com.nestly.application.dto.RespondRequest;
import com.nestly.application.entity.ApplicationStatus;
import com.nestly.application.entity.RentalApplication;
import com.nestly.application.repository.RentalApplicationRepository;
import com.nestly.shared.util.S3PresignHelper;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private static final Set<String> VALID_DECISIONS = Set.of(
            ApplicationStatus.ACCEPTED, ApplicationStatus.DECLINED,
            ApplicationStatus.MORE_INFO_REQUESTED);

    private final RentalApplicationRepository applicationRepository;
    private final ListingClient listingClient;
    private final NotificationClient notificationClient;
    private final S3PresignHelper s3PresignHelper;

    public ApplicationResponse apply(String tenantId, String role, CreateApplicationRequest request) {
        if (!"TENANT".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only tenants can apply to listings");
        }

        ListingDto listing = fetchListing(request.getListingId());
        if (!"APPROVED".equals(listing.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You can only apply to live, approved listings");
        }
        if (applicationRepository.existsByListingIdAndTenantId(
                UUID.fromString(listing.getId()), UUID.fromString(tenantId))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You already applied to this listing");
        }

        RentalApplication application = applicationRepository.save(RentalApplication.builder()
                .listingId(UUID.fromString(listing.getId()))
                .tenantId(UUID.fromString(tenantId))
                .message(request.getMessage())
                .status(ApplicationStatus.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        try {
            notificationClient.notify(NotifyRequest.builder()
                    .userId(listing.getOwnerId())
                    .message("New application for '" + listing.getTitle() + "'")
                    .emailSubject("New application on Nestly")
                    .emailBody("A tenant just applied to your listing '" + listing.getTitle()
                            + "'. Review it in your dashboard.")
                    .build());
        } catch (Exception e) {
            System.out.println("[application-service] could not send application notification: " + e.getMessage());
        }

        return toResponse(application);
    }

    public List<ApplicationResponse> my(String tenantId) {
        return applicationRepository.findByTenantIdOrderByCreatedAtDesc(UUID.fromString(tenantId))
                .stream().map(this::toResponse).toList();
    }

    public List<ApplicationResponse> forListing(String listingId, String userId) {
        ListingDto listing = fetchListing(listingId);
        if (!listing.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your listing");
        }
        return applicationRepository.findByListingIdOrderByCreatedAtDesc(UUID.fromString(listingId))
                .stream().map(this::toResponse).toList();
    }

    public ApplicationResponse respond(String applicationId, String userId, RespondRequest request) {
        RentalApplication application = find(applicationId);
        ListingDto listing = fetchListing(application.getListingId().toString());
        if (!listing.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only the listing owner can respond to applications");
        }
        if (!VALID_DECISIONS.contains(request.getDecision())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "decision must be one of " + VALID_DECISIONS);
        }

        application.setStatus(request.getDecision());
        application.setOwnerResponse(request.getMessage());
        application.setUpdatedAt(Instant.now());
        application = applicationRepository.save(application);

        String note = request.getMessage() == null ? "" : "\n\nMessage from the owner: " + request.getMessage();
        try {
            notificationClient.notify(NotifyRequest.builder()
                    .userId(application.getTenantId().toString())
                    .message("The owner responded to your application for '" + listing.getTitle()
                            + "': " + request.getDecision())
                    .emailSubject("Update on your Nestly application")
                    .emailBody("Your application for '" + listing.getTitle() + "' is now: "
                            + request.getDecision() + note)
                    .build());
        } catch (Exception e) {
            System.out.println("[application-service] could not send response notification: " + e.getMessage());
        }

        return toResponse(application);
    }

    public PresignResponse presign(String applicationId, String tenantId, String filename) {
        RentalApplication application = findOwn(applicationId, tenantId);
        S3PresignHelper.PresignedUpload upload =
                s3PresignHelper.presignPut("applications/" + application.getId(), filename);
        return PresignResponse.builder().key(upload.key()).uploadUrl(upload.url()).build();
    }

    public ApplicationResponse confirm(String applicationId, String tenantId, String key) {
        RentalApplication application = findOwn(applicationId, tenantId);
        application.getDocumentKeys().add(key);
        application.setUpdatedAt(Instant.now());
        return toResponse(applicationRepository.save(application));
    }

    private ListingDto fetchListing(String listingId) {
        try {
            return listingClient.getListing(listingId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Could not reach listing-service: " + e.getMessage());
        }
    }

    private RentalApplication find(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid application id");
        }
        return applicationRepository.findById(uuid).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
    }

    private RentalApplication findOwn(String id, String tenantId) {
        RentalApplication application = find(id);
        if (!application.getTenantId().toString().equals(tenantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your application");
        }
        return application;
    }

    private ApplicationResponse toResponse(RentalApplication application) {
        return ApplicationResponse.builder()
                .id(application.getId().toString())
                .listingId(application.getListingId().toString())
                .tenantId(application.getTenantId().toString())
                .message(application.getMessage())
                .status(application.getStatus())
                .ownerResponse(application.getOwnerResponse())
                .documentUrls(application.getDocumentKeys().stream()
                        .map(s3PresignHelper::presignGet).toList())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
