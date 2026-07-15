package com.nestly.application.controller;

import com.nestly.application.dto.ApplicationResponse;
import com.nestly.application.dto.ConfirmRequest;
import com.nestly.application.dto.CreateApplicationRequest;
import com.nestly.application.dto.PresignRequest;
import com.nestly.application.dto.PresignResponse;
import com.nestly.application.dto.RespondRequest;
import com.nestly.application.service.ApplicationService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Tenant rental applications and owner responses")
@SecurityRequirement(name = "bearerAuth")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Submit a rental application (Tenant only)")
    public ApplicationResponse apply(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody CreateApplicationRequest request) {
        return applicationService.apply(userId, role, request);
    }

    @GetMapping("/my")
    @Operation(summary = "Get my applications (Tenant only)")
    public List<ApplicationResponse> my(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId) {
        return applicationService.my(userId);
    }

    @GetMapping("/listing/{listingId}")
    @Operation(summary = "Get all applications for a listing (Owner only)")
    public List<ApplicationResponse> forListing(
            @PathVariable String listingId,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId) {
        return applicationService.forListing(listingId, userId);
    }

    @PostMapping("/{id}/respond")
    @Operation(summary = "Respond to an application (Owner only)")
    public ApplicationResponse respond(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody RespondRequest request) {
        return applicationService.respond(id, userId, request);
    }

    @PostMapping("/{id}/presign")
    @Operation(summary = "Get a presigned S3 URL to upload an application document (Tenant only)")
    public PresignResponse presign(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody PresignRequest request) {
        return applicationService.presign(id, userId, request.getFilename());
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm an application document upload (Tenant only)")
    public ApplicationResponse confirm(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody ConfirmRequest request) {
        return applicationService.confirm(id, userId, request.getKey());
    }
}
