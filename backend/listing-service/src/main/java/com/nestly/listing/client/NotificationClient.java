package com.nestly.listing.client;

import com.nestly.listing.dto.NotifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${clients.notification-service-url}")
public interface NotificationClient {

    @PostMapping("/internal/notifications/notify")
    void notify(@RequestBody NotifyRequest request);
}
