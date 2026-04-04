package br.com.joaopedroafluz.shared.domain;

import java.util.UUID;

public record AdoptionMessage(
        UUID petId,
        String petName,
        UserDTO user
) {

}
