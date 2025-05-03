package br.com.joaopedroafluz.notificationservice.service;

import br.com.joaopedroafluz.notificationservice.domain.AdoptionMessage;
import br.com.joaopedroafluz.notificationservice.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    @RabbitListener(queues = {"${rabbit.queue}"})
    public void handleAdoptionMessage(@Payload String message) {
        var adoptionMessage = JsonUtils.fromJson(message, AdoptionMessage.class);

        var subject = "Adoption notification";
        var body = String.format("Congratulations, %s, you have successfully adopted %s.", adoptionMessage.userName(),
                adoptionMessage.petName());

        emailService.send(adoptionMessage.userEmail(), subject, body);
    }

}
