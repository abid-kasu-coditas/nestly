package com.nestly.listing.service;

import com.nestly.listing.client.NotificationClient;
import com.nestly.listing.dto.CreateListingRequest;
import com.nestly.listing.dto.ListingResponse;
import com.nestly.listing.dto.NotifyRequest;
import com.nestly.listing.dto.PresignResponse;
import com.nestly.listing.entity.Listing;
import com.nestly.listing.entity.ListingStatus;
import com.nestly.listing.repository.ListingRepository;
import com.nestly.shared.util.S3PresignHelper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final NotificationClient notificationClient;
    private final S3PresignHelper s3PresignHelper;

    public ListingResponse create(String ownerId, String role, CreateListingRequest request) {
        requireRole(role, "OWNER", "Only owners can create listings");
        Listing listing = listingRepository.save(Listing.builder()
                .ownerId(UUID.fromString(ownerId))
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .rent(request.getRent())
                .rooms(request.getRooms())
                .amenities(request.getAmenities() == null ? List.of() : request.getAmenities())
                .status(ListingStatus.PENDING_VERIFICATION)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        return toResponse(listing);
    }

    public ListingResponse update(String id, String ownerId, CreateListingRequest request) {
        Listing listing = findOwned(id, ownerId);
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setLocation(request.getLocation());
        listing.setRent(request.getRent());
        listing.setRooms(request.getRooms());
        if (request.getAmenities() != null) {
            listing.setAmenities(request.getAmenities());
        }
        listing.setStatus(ListingStatus.PENDING_VERIFICATION);
        listing.setUpdatedAt(Instant.now());
        listing = listingRepository.save(listing);

        return toResponse(listing);
    }

    public ListingResponse get(String id) {
        return toResponse(find(id));
    }

    public List<ListingResponse> getMy(String ownerId) {
        return listingRepository.findByOwnerIdOrderByCreatedAtDesc(UUID.fromString(ownerId))
                .stream().map(this::toResponse).toList();
    }

    public List<ListingResponse> browse() {
        return listingRepository.findByStatusOrderByCreatedAtDesc(ListingStatus.APPROVED)
                .stream().map(this::toResponse).toList();
    }

    public List<ListingResponse> pendingReview(String role) {
        requireRole(role, "ADMIN", "Only admins can view the review queue");
        return listingRepository.findByStatusOrderByCreatedAtDesc(ListingStatus.PENDING_VERIFICATION)
                .stream().map(this::toResponse).toList();
    }

    public PresignResponse presignPhoto(String id, String ownerId, String filename) {
        Listing listing = findOwned(id, ownerId);
        S3PresignHelper.PresignedUpload upload =
                s3PresignHelper.presignPut("listings/" + listing.getId(), filename);
        return PresignResponse.builder().key(upload.key()).uploadUrl(upload.url()).build();
    }

    public ListingResponse confirmPhoto(String id, String ownerId, String key) {
        Listing listing = findOwned(id, ownerId);
        listing.getPhotoKeys().add(key);
        listing.setUpdatedAt(Instant.now());
        return toResponse(listingRepository.save(listing));
    }

    public PresignResponse presignDocument(String id, String ownerId, String filename) {
        Listing listing = findOwned(id, ownerId);
        S3PresignHelper.PresignedUpload upload =
                s3PresignHelper.presignPut("listings/" + listing.getId(), filename);
        return PresignResponse.builder().key(upload.key()).uploadUrl(upload.url()).build();
    }

    public ListingResponse confirmDocument(String id, String ownerId, String key) {
        Listing listing = findOwned(id, ownerId);
        listing.getDocumentKeys().add(key);
        listing.setUpdatedAt(Instant.now());
        return toResponse(listingRepository.save(listing));
    }

    public ListingResponse decide(String id, String role, String decision, String comment) {
        requireRole(role, "ADMIN", "Only admins can decide on listings");
        if (!List.of(ListingStatus.APPROVED, ListingStatus.REJECTED,
                ListingStatus.CHANGES_REQUESTED).contains(decision)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported listing decision");
        }
        Listing listing = find(id);
        listing.setStatus(decision);
        listing.setAdminComment(comment);
        listing.setUpdatedAt(Instant.now());
        listing = listingRepository.save(listing);

        String commentPart = comment == null ? "" : "\n\nComment: " + comment;
        try {
            notificationClient.notify(NotifyRequest.builder()
                    .userId(listing.getOwnerId().toString())
                    .message("Admin decision on '" + listing.getTitle() + "': " + decision)
                    .emailSubject("Update on your Nestly listing")
                    .emailBody("Your listing '" + listing.getTitle() + "' is now: " + decision + commentPart)
                    .build());
        } catch (Exception e) {
            System.out.println("[listing-service] could not send decision notification: " + e.getMessage());
        }

        return toResponse(listing);
    }

    private void requireRole(String role, String required, String message) {
        if (!required.equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }
    }

    private Listing find(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid listing id");
        }
        return listingRepository.findById(uuid).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));
    }

    private Listing findOwned(String id, String ownerId) {
        Listing listing = find(id);
        if (!listing.getOwnerId().toString().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your listing");
        }
        return listing;
    }

    private ListingResponse toResponse(Listing listing) {
        return ListingResponse.builder()
                .id(listing.getId().toString())
                .ownerId(listing.getOwnerId().toString())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .location(listing.getLocation())
                .rent(listing.getRent())
                .rooms(listing.getRooms())
                .amenities(listing.getAmenities())
                .photoUrls(listing.getPhotoKeys().stream().map(s3PresignHelper::presignGet).toList())
                .documentUrls(listing.getDocumentKeys().stream().map(s3PresignHelper::presignGet).toList())
                .status(listing.getStatus())
                .adminComment(listing.getAdminComment())
                .createdAt(listing.getCreatedAt())
                .updatedAt(listing.getUpdatedAt())
                .build();
    }
}
