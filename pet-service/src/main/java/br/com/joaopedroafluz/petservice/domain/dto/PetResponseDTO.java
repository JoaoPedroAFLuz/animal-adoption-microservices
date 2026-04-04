package br.com.joaopedroafluz.petservice.domain.dto;

import br.com.joaopedroafluz.petservice.domain.enums.Gender;
import br.com.joaopedroafluz.petservice.domain.enums.Size;
import br.com.joaopedroafluz.petservice.domain.enums.Specie;
import br.com.joaopedroafluz.petservice.domain.enums.Status;
import br.com.joaopedroafluz.petservice.domain.model.Pet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PetResponseDTO(
        UUID id,
        UUID ownerId,
        String name,
        String description,
        Specie specie,
        String breed,
        Size size,
        Status status,
        Gender gender,
        LocalDate birthDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static PetResponseDTO from(Pet pet) {
        return new PetResponseDTO(
                pet.getId(),
                pet.getOwnerId(),
                pet.getName(),
                pet.getDescription(),
                pet.getSpecie(),
                pet.getBreed(),
                pet.getSize(),
                pet.getStatus(),
                pet.getGender(),
                pet.getBirthDate(),
                pet.getCreatedAt(),
                pet.getUpdatedAt()
        );
    }

}
