package br.com.joaopedroafluz.petservice.domain.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PetInputDTO(
        UUID id,
        String name,
        String description,
        String specie,
        String breed,
        String size,
        String status,
        String gender,
        LocalDate birthDate
) {
}
