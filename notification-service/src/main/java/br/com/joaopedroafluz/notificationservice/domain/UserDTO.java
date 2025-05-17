package br.com.joaopedroafluz.notificationservice.domain;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email
) {
}
