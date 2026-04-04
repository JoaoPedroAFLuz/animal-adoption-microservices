package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.domain.dto.*;
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

    public Page<Pet> findByOwnerId(UUID userId, Pageable pageable) {
        return petRepository.findByOwnerId(userId, pageable);
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
    public Pet save(PetRegistrationInputDTO newPetDTO) {
        var pet = convertInputDTOToModel(newPetDTO);

        return save(pet);
    }

    @Transactional
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    @Transactional
    @CacheEvict(value = "featured-pets", allEntries = true)
    public Pet update(UUID id, PetUpdateInputDTO petUpdateInputDTO) {
        var petFound = findByIdOrThrow(id);

        petFound.setName(petUpdateInputDTO.name());
        petFound.setDescription(petUpdateInputDTO.description());
        petFound.setSpecie(petUpdateInputDTO.specie());
        petFound.setBreed(petUpdateInputDTO.breed());
        petFound.setSize(petUpdateInputDTO.size());
        petFound.setGender(petUpdateInputDTO.gender());
        petFound.setBirthDate(petUpdateInputDTO.birthDate());

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
        findByIdOrThrow(id);
        petRepository.deleteById(id);
    }

    private Pet convertInputDTOToModel(PetRegistrationInputDTO petRegistrationInputDTO) {
        return Pet.builder()
                .name(petRegistrationInputDTO.name())
                .description(petRegistrationInputDTO.description())
                .specie(petRegistrationInputDTO.specie())
                .breed(petRegistrationInputDTO.breed())
                .size(petRegistrationInputDTO.size())
                .gender(petRegistrationInputDTO.gender())
                .birthDate(petRegistrationInputDTO.birthDate())
                .status(Status.AVAILABLE)
                .build();
    }

}
