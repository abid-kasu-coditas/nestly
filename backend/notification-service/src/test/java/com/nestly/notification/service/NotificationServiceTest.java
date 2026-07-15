package com.nestly.notification.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nestly.notification.client.UserClient;
import com.nestly.notification.dto.UserDto;
import com.nestly.notification.repository.InAppNotificationRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private InAppNotificationRepository notificationRepository;
    @Mock
    private UserClient userClient;
    @Mock
    private EmailService emailService;

    private NotificationService notificationService;

    private final String userId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository, userClient, emailService);
    }

    @Test
    void notifyUserSavesRowAndSendsEmail() {
        when(userClient.getUser(userId)).thenReturn(
                UserDto.builder().id(userId).email("owner@test.com").build());

        notificationService.notifyUser(userId, "New application", "Subject", "Body");

        verify(notificationRepository).save(any());
        verify(emailService).send("owner@test.com", "Subject", "Body");
    }

    @Test
    void inAppRowSurvivesWhenEmailLookupFails() {
        when(userClient.getUser(userId)).thenThrow(new RuntimeException("auth-service down"));

        assertThatCode(() -> notificationService.notifyUser(userId, "msg", "Subject", "Body"))
                .doesNotThrowAnyException();

        verify(notificationRepository).save(any());
        verify(emailService, never()).send(any(), any(), any());
    }
}
