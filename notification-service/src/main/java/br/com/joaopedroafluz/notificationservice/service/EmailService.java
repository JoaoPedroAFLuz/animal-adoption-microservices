package br.com.joaopedroafluz.notificationservice.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendHtml(String to, String subject, String template, Map<String, Object> variables) {
        final var context = new Context();
        context.setVariables(variables);

        final var html = templateEngine.process(template, context);

        try {
            final var mimeMessage = mailSender.createMimeMessage();
            final var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.setFrom("no-reply@adoption.com.br");

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
