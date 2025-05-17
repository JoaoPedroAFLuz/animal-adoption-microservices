package br.com.joaopedroafluz.petservice.domain.dto;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email
) {
}
