package br.com.joaopedroafluz.petservice.domain.dtos;

public record UserDTO(
        Long id,
        String name,
        String email
) {
}
