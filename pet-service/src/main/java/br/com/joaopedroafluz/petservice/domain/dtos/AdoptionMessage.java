package br.com.joaopedroafluz.petservice.domain.dtos;

public record AdoptionMessage(
        Long petId,
        String petName,
        Long userId,
        String userEmail
) {
}