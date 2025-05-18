package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.domain.dto.AdoptionMessage;
import br.com.joaopedroafluz.petservice.domain.dto.PetFilter;
import br.com.joaopedroafluz.petservice.domain.dto.PetInputDTO;
import br.com.joaopedroafluz.petservice.domain.dto.UserDTO;
import br.com.joaopedroafluz.petservice.domain.enums.Gender;
import br.com.joaopedroafluz.petservice.domain.enums.Size;
import br.com.joaopedroafluz.petservice.domain.enums.Specie;
import br.com.joaopedroafluz.petservice.domain.enums.Status;
import br.com.joaopedroafluz.petservice.domain.exception.PetAlreadyAdoptedException;
import br.com.joaopedroafluz.petservice.domain.exception.PetNotFoundException;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import br.com.joaopedroafluz.petservice.domain.repository.PetRepository;
import br.com.joaopedroafluz.petservice.domain.repository.specification.PetSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final NotificationService notificationService;

    public Page<Pet> findAll(PetFilter petFilter, Pageable pageable) {
        final var petSpecification = PetSpecification.withFilters(petFilter);

        return petRepository.findAll(petSpecification, pageable);
    }

    @Cacheable("featured-pets")
    public List<Pet> findFeatured() {
        return petRepository.findFeatured(PageRequest.of(0, 10));
    }

    public Optional<Pet> findById(UUID id) {
        return petRepository.findById(id);
    }

    public Pet findByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> new PetNotFoundException(id));
    }

    public List<Pet> findByOwnerId(UUID userId) {
        return petRepository.findByOwnerId(userId);
    }

    public List<String> findSpecies() {
        return Arrays.stream(Specie.values())
                .map(Enum::name)
                .toList();
    }

    public List<String> findSizes() {
        return Arrays.stream(Size.values())
                .map(Enum::name)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "featured-pets", allEntries = true)
    public Pet save(PetInputDTO newPetDTO) {
        var pet = convertInputDTOToModel(newPetDTO);

        return save(pet);
    }

    @Transactional
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    @Transactional
    @CacheEvict(value = "featured-pets", allEntries = true)
    public Pet update(UUID id, PetInputDTO petInputDTO) {
        var petFound = findByIdOrThrow(id);

        petFound.setName(petInputDTO.name());
        petFound.setDescription(petInputDTO.description());
        petFound.setSpecie(Specie.valueOf(petInputDTO.specie()));
        petFound.setBreed(petInputDTO.breed());
        petFound.setSize(Size.valueOf(petInputDTO.size()));
        petFound.setGender(Gender.valueOf(petInputDTO.gender()));
        petFound.setBirthDate(petInputDTO.birthDate());

        return save(petFound);
    }

    @Transactional
    @CacheEvict(value = "featured-pets", allEntries = true)
    public Pet adopt(UUID petId, UserDTO user) {
        var pet = findByIdOrThrow(petId);

        if (pet.getOwnerId() != null || Status.ADOPTED.equals(pet.getStatus())) {
            throw new PetAlreadyAdoptedException();
        }

        pet.setOwnerId(user.id());
        pet.setStatus(Status.ADOPTED);

        pet = save(pet);

        notificationService.sendAdoptionNotification(new AdoptionMessage(pet.getId(), pet.getName(), user));

        return pet;
    }

    @CacheEvict(value = "featured-pets", allEntries = true)
    public void deleteById(UUID id) {
        petRepository.deleteById(id);
    }

    private Pet convertInputDTOToModel(PetInputDTO petInputDTO) {
        return Pet.builder()
                .id(petInputDTO.id())
                .name(petInputDTO.name())
                .description(petInputDTO.description())
                .specie(Specie.valueOf(petInputDTO.specie()))
                .breed(petInputDTO.breed())
                .size(Size.valueOf(petInputDTO.size()))
                .status(Status.valueOf(petInputDTO.status()))
                .gender(Gender.valueOf(petInputDTO.gender()))
                .birthDate(petInputDTO.birthDate())
                .build();
    }

}
