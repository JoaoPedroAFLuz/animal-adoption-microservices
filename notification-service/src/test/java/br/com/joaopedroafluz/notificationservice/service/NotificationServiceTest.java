package br.com.joaopedroafluz.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldSendEmailWhenAdoptionMessageReceived() {
        final var message = """
                {"petId":"123e4567-e89b-12d3-a456-426614174000","petName":"Rex","user":{"id":"123e4567-e89b-12d3-a456-426614174001","name":"João","email":"joao@email.com"}}""";

        notificationService.handleAdoptionMessage(message);

        verify(emailService, times(1)).sendHtml(
                eq("joao@email.com"),
                eq("Adoption confirmation"),
                eq("adoption-email"),
                eq(Map.of("userName", "João", "petName", "Rex"))
        );
    }

    @Test
    void shouldThrowExceptionWhenMessageIsInvalid() {
        final var invalidMessage = "invalid json";

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> notificationService.handleAdoptionMessage(invalidMessage)
        );

        verify(emailService, never()).sendHtml(any(), any(), any(), any());
    }

}
