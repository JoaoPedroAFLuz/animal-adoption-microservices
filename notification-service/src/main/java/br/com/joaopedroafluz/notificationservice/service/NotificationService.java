package br.com.joaopedroafluz.notificationservice.service;

import br.com.joaopedroafluz.shared.domain.AdoptionMessage;
import br.com.joaopedroafluz.shared.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    @RabbitListener(queues = {"${rabbit.queue}"})
    public void handleAdoptionMessage(@Payload String message) {
        log.info("Received adoption message: {}", message);

        var adoptionMessage = JsonUtils.fromJson(message, AdoptionMessage.class);

        emailService.sendHtml(
                adoptionMessage.user().email(),
                "Adoption confirmation",
                "adoption-email",
                Map.of("userName", adoptionMessage.user().name(), "petName", adoptionMessage.petName())
        );

        log.info("Adoption email sent to {} for pet {}", adoptionMessage.user().email(), adoptionMessage.petName());
    }

}
