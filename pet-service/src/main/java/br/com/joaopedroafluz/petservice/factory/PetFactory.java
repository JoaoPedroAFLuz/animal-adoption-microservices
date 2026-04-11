package br.com.joaopedroafluz.petservice.factory;

import br.com.joaopedroafluz.petservice.domain.dto.PetRegistrationInputDTO;
import br.com.joaopedroafluz.petservice.domain.dto.PetUpdateInputDTO;
import br.com.joaopedroafluz.petservice.domain.enums.Gender;
import br.com.joaopedroafluz.petservice.domain.enums.Size;
import br.com.joaopedroafluz.petservice.domain.enums.Specie;
import br.com.joaopedroafluz.petservice.domain.enums.Status;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class PetFactory {

    public static Pet createDefaultPet() {
        return Pet.builder()
                  .id(UUID.randomUUID())
                  .name("Rex")
                  .description(null)
                  .specie(Specie.DOG)
                  .breed("SRD")
                  .size(Size.MEDIUM)
                  .status(Status.AVAILABLE)
                  .gender(Gender.MALE)
                  .birthDate(LocalDate.of(2025, 1, 1))
                  .createdAt(LocalDateTime.now())
                  .updatedAt(null)
                  .version(0L)
                  .build();
    }

    public static Pet createAdoptedPet(UUID ownerId) {
        return Pet.builder()
                  .id(UUID.randomUUID())
                  .name("Luna")
                  .description(null)
                  .specie(Specie.CAT)
                  .breed("Persa")
                  .size(Size.SMALL)
                  .status(Status.ADOPTED)
                  .gender(Gender.FEMALE)
                  .birthDate(LocalDate.of(2021, 3, 20))
                  .ownerId(ownerId)
                  .createdAt(LocalDateTime.now())
                  .updatedAt(LocalDateTime.now())
                  .version(1L)
                  .build();
    }

    public static PetRegistrationInputDTO createDefaultPetRegistrationInputDTO() {
        return new PetRegistrationInputDTO("Bob",
                                           "A friendly dog",
                                           Specie.DOG,
                                           "Labrador",
                                           Size.MEDIUM,
                                           Gender.MALE,
                                           LocalDate.of(2020, 1, 1));
    }

    public static PetUpdateInputDTO createDefaultPetUpdateInputDTO() {
        return new PetUpdateInputDTO("Bob",
                                     "A friendly dog",
                                     Specie.DOG,
                                     "Labrador",
                                     Size.MEDIUM,
                                     Gender.MALE,
                                     LocalDate.of(2020, 1, 1));
    }

}