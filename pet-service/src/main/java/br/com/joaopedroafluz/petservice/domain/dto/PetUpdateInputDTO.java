package br.com.joaopedroafluz.petservice.domain.dto;

import br.com.joaopedroafluz.petservice.domain.enums.Gender;
import br.com.joaopedroafluz.petservice.domain.enums.Size;
import br.com.joaopedroafluz.petservice.domain.enums.Specie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

public record PetUpdateInputDTO(

        @NotBlank(message = "Name is required.")
        @jakarta.validation.constraints.Size(max = 50, message = "Name must be at most 50 characters.")
        String name,

        @jakarta.validation.constraints.Size(max = 255, message = "Description must be at most 255 characters.")
        String description,

        @NotNull(message = "Specie is required.")
        Specie specie,

        @NotBlank(message = "Breed is required.")
        String breed,

        @NotNull(message = "Size is required.")
        Size size,

        @NotNull(message = "Gender is required.")
        Gender gender,

        @NotNull(message = "Birth date is required.")
        @PastOrPresent(message = "Birth date must be in the past or present.")
        LocalDate birthDate
) {
}