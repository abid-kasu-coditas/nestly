package com.nestly.notification.service;

import com.nestly.notification.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserClient userClient;
    private final EmailService emailService;

    public void notifyUser(String userId, String message, String emailSubject, String emailBody) {

        if (emailSubject == null) {
            return;
        }
        String email = findUserEmail(userId);
        if (email != null) {
            emailService.send(email, emailSubject, emailBody);
        }
    }

    private String findUserEmail(String userId) {
        try {
            return userClient.getUser(userId).getEmail();
        } catch (Exception e) {
            System.out.println("[notification-service] could not resolve email for "
                    + userId + ": " + e.getMessage());
            return null;
        }
    }
}
