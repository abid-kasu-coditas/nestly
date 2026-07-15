package com.nestly.notification.controller;

import com.nestly.notification.dto.NotifyRequest;
import com.nestly.notification.dto.OtpEmailRequest;
import com.nestly.notification.service.EmailService;
import com.nestly.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
 public class InternalNotificationController {

    private final NotificationService notificationService;
    private final EmailService emailService;

    @PostMapping("/notify")
     public void notify(@RequestBody NotifyRequest request) {
        notificationService.notifyUser(
                request.getUserId(),
                request.getMessage(),
                request.getEmailSubject(),
                request.getEmailBody()
        );
    }

    @PostMapping("/otp-email")
     public void sendOtpEmail(@RequestBody OtpEmailRequest request) {
        emailService.send(request.getEmail(),
                "Your Nestly login code",
                "Your one-time login code is " + request.getOtp()
                        + ". It expires in " + request.getExpiryMinutes() + " minutes.");
    }
}
