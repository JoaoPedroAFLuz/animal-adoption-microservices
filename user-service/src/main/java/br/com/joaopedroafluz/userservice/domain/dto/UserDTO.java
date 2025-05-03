package br.com.joaopedroafluz.userservice.domain.dto;

public record UserDTO(
        Long id,
        String name,
        String email
) {
}
