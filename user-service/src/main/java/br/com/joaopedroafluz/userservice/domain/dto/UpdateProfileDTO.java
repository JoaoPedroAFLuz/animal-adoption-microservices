package br.com.joaopedroafluz.userservice.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileDTO(
        @NotBlank String firstName,
        @NotBlank String lastName
) {
}
