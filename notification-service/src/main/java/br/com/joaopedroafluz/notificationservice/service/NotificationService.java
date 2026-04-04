package br.com.joaopedroafluz.notificationservice.service;

import br.com.joaopedroafluz.notificationservice.domain.AdoptionMessage;
import br.com.joaopedroafluz.notificationservice.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    @RabbitListener(queues = {"${rabbit.queue}"})
    public void handleAdoptionMessage(@Payload String message) {
        log.info("Received adoption message: {}", message);

        var adoptionMessage = JsonUtils.fromJson(message, AdoptionMessage.class);

        var subject = "Adoption confirmation";
        var body = String.format("Congratulations, %s, you have successfully adopted %s.",
                adoptionMessage.user().name(),
                adoptionMessage.petName());

        emailService.send(adoptionMessage.user().email(), subject, body);

        log.info("Adoption email sent to {} for pet {}", adoptionMessage.user().email(), adoptionMessage.petName());
    }

}
