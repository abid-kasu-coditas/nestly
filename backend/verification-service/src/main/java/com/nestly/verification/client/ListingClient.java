package com.nestly.verification.client;

import com.nestly.verification.dto.DecisionRequest;
import com.nestly.verification.dto.ListingDecisionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "listing-service", url = "${clients.listing-service-url}")
public interface ListingClient {

    @PostMapping("/listings/internal/{listingId}/decision")
    ListingDecisionResponse decide(
            @PathVariable String listingId,
            @RequestHeader("X-User-Role") String role,
            @RequestHeader("X-Verification-Decision") String decision,
            @RequestBody(required = false) DecisionRequest request);
}
