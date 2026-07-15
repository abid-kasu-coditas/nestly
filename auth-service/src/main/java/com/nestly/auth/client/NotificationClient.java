package com.nestly.auth.client;

import com.nestly.auth.dto.NotifyRequest;
import com.nestly.auth.dto.OtpEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${clients.notification-service-url}")
public interface NotificationClient {

    @PostMapping("/internal/notifications/notify")
    void notify(@RequestBody NotifyRequest request);

    @PostMapping("/internal/notifications/otp-email")
    void sendOtpEmail(@RequestBody OtpEmailRequest request);
}
