package br.com.joaopedroafluz.petservice.domain.dtos;

import java.time.LocalDate;

public record PetInputDTO(
        Long id,
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
