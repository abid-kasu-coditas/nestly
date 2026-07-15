package com.nestly.verification.controller;

import com.nestly.verification.client.ListingClient;
import com.nestly.verification.dto.DecisionRequest;
import com.nestly.verification.dto.ListingDecisionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
@Tag(name = "Verification", description = "Manual listing-verification decisions")
public class VerificationController {

    private static final String ADMIN = "ADMIN";

    private final ListingClient listingClient;

    @PostMapping("/{listingId}/accept")
    @Operation(summary = "Accept a listing (Admin only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ListingDecisionResponse accept(
            @PathVariable String listingId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestBody(required = false) DecisionRequest request) {
        return decide(listingId, role, "APPROVED", request);
    }

    @PostMapping("/{listingId}/reject")
    @Operation(summary = "Reject a listing (Admin only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ListingDecisionResponse reject(
            @PathVariable String listingId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestBody(required = false) DecisionRequest request) {
        return decide(listingId, role, "REJECTED", request);
    }

    @PostMapping("/{listingId}/send-back")
    @Operation(summary = "Send a listing back to its owner for changes (Admin only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ListingDecisionResponse sendBack(
            @PathVariable String listingId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestBody(required = false) DecisionRequest request) {
        return decide(listingId, role, "CHANGES_REQUESTED", request);
    }

    private ListingDecisionResponse decide(
            String listingId, String role, String decision, DecisionRequest request) {
        if (!ADMIN.equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can perform this action");
        }
        return listingClient.decide(listingId, ADMIN, decision, request);
    }
}
