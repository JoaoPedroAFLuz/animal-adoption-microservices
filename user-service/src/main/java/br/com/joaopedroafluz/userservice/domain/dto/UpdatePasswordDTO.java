package br.com.joaopedroafluz.userservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(
        @NotBlank String currentPassword,
        @NotBlank @Size(min = 8) String newPassword
) {
}
