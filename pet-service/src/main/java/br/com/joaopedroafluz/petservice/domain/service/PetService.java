package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.domain.dtos.PetInputDTO;
import br.com.joaopedroafluz.petservice.domain.enums.Gender;
import br.com.joaopedroafluz.petservice.domain.enums.Size;
import br.com.joaopedroafluz.petservice.domain.enums.Status;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import br.com.joaopedroafluz.petservice.domain.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public Optional<Pet> findById(Long id) {
        return petRepository.findById(id);
    }

    public Pet findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Pet save(PetInputDTO newPetDTO) {
        var pet = convertInputDtoToModel(newPetDTO);

        return save(pet);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet update(Long id, PetInputDTO petInputDTO) {
        var petFound = findByIdOrThrow(id);

        petFound.setName(petInputDTO.name());
        petFound.setDescription(petInputDTO.description());
        petFound.setBreed(petInputDTO.breed());
        petFound.setSize(Size.valueOf(petInputDTO.size()));
        petFound.setStatus(Status.valueOf(petInputDTO.status()));
        petFound.setGender(Gender.valueOf(petInputDTO.gender()));
        petFound.setBirthDate(petInputDTO.birthDate());

        return save(petFound);
    }

    public void deleteById(Long id) {
        petRepository.deleteById(id);
    }

    private static Pet convertInputDtoToModel(PetInputDTO petInputDTO) {
        return Pet.builder()
                .id(petInputDTO.id())
                .name(petInputDTO.name())
                .description(petInputDTO.description())
                .breed(petInputDTO.breed())
                .size(Size.valueOf(petInputDTO.size()))
                .status(Status.AVAILABLE)
                .gender(Gender.valueOf(petInputDTO.gender()))
                .birthDate(petInputDTO.birthDate())
                .build();
    }

}
