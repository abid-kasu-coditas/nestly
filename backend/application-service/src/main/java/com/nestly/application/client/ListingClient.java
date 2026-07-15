package com.nestly.application.client;

import com.nestly.application.dto.ListingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "listing-service", url = "${clients.listing-service-url}")
public interface ListingClient {

    @GetMapping("/listings/{id}")
    ListingDto getListing(@PathVariable("id") String id);
}
