package br.com.joaopedroafluz.notificationservice.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @Test
    void shouldSendHtmlEmail() {
        final var mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("adoption-email"), any(Context.class))).thenReturn("<html>test</html>");

        emailService.sendHtml("joao@email.com", "Subject", "adoption-email", Map.of("key", "value"));

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void shouldThrowWhenMailSenderFails() {
        final var mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("<html>test</html>");
        doThrow(new org.springframework.mail.MailSendException("SMTP error"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThrows(
                org.springframework.mail.MailSendException.class,
                () -> emailService.sendHtml("joao@email.com", "Subject", "template", Map.of())
        );
    }

}
