package br.com.joaopedroafluz.shared.domain;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email
) {

}
