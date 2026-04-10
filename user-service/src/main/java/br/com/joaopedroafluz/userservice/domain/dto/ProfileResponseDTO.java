package br.com.joaopedroafluz.userservice.domain.dto;

public record ProfileResponseDTO(
        String id,
        String firstName,
        String lastName,
        String email,
        String picture
) {
}
