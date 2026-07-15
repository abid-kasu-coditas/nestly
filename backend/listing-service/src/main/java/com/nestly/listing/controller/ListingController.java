package com.nestly.listing.controller;

import com.nestly.listing.dto.ConfirmRequest;
import com.nestly.listing.dto.CreateListingRequest;
import com.nestly.listing.dto.DecisionRequest;
import com.nestly.listing.dto.ListingResponse;
import com.nestly.listing.dto.PresignRequest;
import com.nestly.listing.dto.PresignResponse;
import com.nestly.listing.service.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
@Tag(name = "Listings", description = "Property listing management and browsing")
public class ListingController {

    private final ListingService listingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a listing for manual admin verification", security = @SecurityRequirement(name = "bearerAuth"))
    public ListingResponse create(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody CreateListingRequest request) {
        return listingService.create(userId, role, request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update and resubmit a listing for manual verification", security = @SecurityRequirement(name = "bearerAuth"))
    public ListingResponse update(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CreateListingRequest request) {
        return listingService.update(id, userId, request);
    }

    @GetMapping
    @Operation(summary = "Browse approved listings")
    public List<ListingResponse> browse() {
        return listingService.browse();
    }

    @GetMapping("/my")
    @Operation(summary = "Get my listings", security = @SecurityRequirement(name = "bearerAuth"))
    public List<ListingResponse> my(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId) {
        return listingService.getMy(userId);
    }

    @GetMapping("/pending-review")
    @Operation(summary = "Get listings awaiting manual verification", security = @SecurityRequirement(name = "bearerAuth"))
    public List<ListingResponse> pendingReview(
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role) {
        return listingService.pendingReview(role);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a listing")
    public ListingResponse get(@PathVariable String id) {
        return listingService.get(id);
    }

    @PostMapping("/{id}/photos/presign")
    @Operation(summary = "Get a presigned photo-upload URL", security = @SecurityRequirement(name = "bearerAuth"))
    public PresignResponse presignPhoto(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody PresignRequest request) {
        return listingService.presignPhoto(id, userId, request.getFilename());
    }

    @PostMapping("/{id}/photos/confirm")
    @Operation(summary = "Confirm a photo upload", security = @SecurityRequirement(name = "bearerAuth"))
    public ListingResponse confirmPhoto(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody ConfirmRequest request) {
        return listingService.confirmPhoto(id, userId, request.getKey());
    }

    @PostMapping("/{id}/documents/presign")
    @Operation(summary = "Get a presigned document-upload URL", security = @SecurityRequirement(name = "bearerAuth"))
    public PresignResponse presignDocument(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody PresignRequest request) {
        return listingService.presignDocument(id, userId, request.getFilename());
    }

    @PostMapping("/{id}/documents/confirm")
    @Operation(summary = "Confirm a document upload", security = @SecurityRequirement(name = "bearerAuth"))
    public ListingResponse confirmDocument(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody ConfirmRequest request) {
        return listingService.confirmDocument(id, userId, request.getKey());
    }

    @PostMapping("/internal/{id}/decision")
    @Operation(summary = "Apply an admin verification decision")
    public ListingResponse decide(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestHeader("X-Verification-Decision") String decision,
            @RequestBody(required = false) DecisionRequest request) {
        return listingService.decide(id, role, decision, request == null ? null : request.getComment());
    }
}
