package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.domain.dto.AdoptionMessage;
import br.com.joaopedroafluz.petservice.domain.dto.PetFilter;
import br.com.joaopedroafluz.petservice.domain.dto.UserDTO;
import br.com.joaopedroafluz.petservice.domain.enums.Gender;
import br.com.joaopedroafluz.petservice.domain.enums.Size;
import br.com.joaopedroafluz.petservice.domain.enums.Specie;
import br.com.joaopedroafluz.petservice.domain.enums.Status;
import br.com.joaopedroafluz.petservice.domain.exception.PetAlreadyAdoptedException;
import br.com.joaopedroafluz.petservice.domain.exception.PetNotFoundException;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import br.com.joaopedroafluz.petservice.domain.repository.PetRepository;
import br.com.joaopedroafluz.petservice.factory.PetFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private NotificationService notificationService;

    @Spy
    @InjectMocks
    private PetService petService;

    @Test
    void shouldReturnFilteredPets() {
        final var filter = new PetFilter();
        final var pageable = PageRequest.of(0, 10);
        final var pets = List.of(PetFactory.createDefaultPet());
        final var expectedPage = new PageImpl<>(pets, pageable, pets.size());

        when(petRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(expectedPage);

        final var result = petService.findAll(filter, pageable);

        assertEquals(1, result.getTotalElements());
        verify(petRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldReturnFeaturedPets() {
        final var pet = PetFactory.createDefaultPet();
        final var pets = List.of(pet);

        when(petRepository.findFeatured(any()))
                .thenReturn(pets);

        final var result = petService.findFeatured();

        assertEquals(1, result.size());
        assertEquals(pets, result);
        assertEquals(pet.getName(), result.getFirst().getName());
        verify(petRepository).findFeatured(any());
    }

    @Test
    void shouldCacheFeaturedPets() {
        final var first = petService.findFeatured();

        petRepository.save(PetFactory.createDefaultPet());

        final var second = petService.findFeatured();

        assertEquals(first, second);
    }

    @Test
    void shouldReturnPetWhenIdExists() {
        final var pet = PetFactory.createDefaultPet();

        when(petRepository.findById(eq(pet.getId()))).thenReturn(Optional.of(pet));

        final var result = petService.findById(pet.getId());

        assertTrue(result.isPresent());
        assertEquals(pet, result.get());

        verify(petRepository, times(1)).findById(eq(pet.getId()));
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        final var randomId = UUID.randomUUID();

        when(petRepository.findById(eq(randomId))).thenReturn(Optional.empty());

        final var result = petService.findById(randomId);

        assertTrue(result.isEmpty());

        verify(petRepository, times(1)).findById(eq(randomId));
    }

    @Test
    void shouldThrowExceptionWhenPetNotFound() {
        final var randomId = UUID.randomUUID();

        when(petRepository.findById(eq(randomId))).thenReturn(Optional.empty());

        final var exception = assertThrows(PetNotFoundException.class, () ->
                petService.findByIdOrThrow(randomId));

        assertEquals(String.format("Pet with id %s not found.", randomId), exception.getMessage());
        verify(petRepository, times(1)).findById(eq(randomId));
    }

    @Test
    void shouldReturnPetsByOwnerIdIfExists() {
        final var ownerId = UUID.randomUUID();
        final var pets = List.of(PetFactory.createAdoptedPet(ownerId));

        when(petRepository.findByOwnerId(eq(ownerId))).thenReturn(pets);

        final var result = petService.findByOwnerId(ownerId);

        assertEquals(1, result.size());
        assertEquals(pets, result);
        verify(petRepository, times(1)).findByOwnerId(eq(ownerId));
    }

    @Test
    void shouldReturnAllSpeciesNames() {
        final var species = petService.findSpecies();

        assertEquals(Specie.values().length, species.size());

        for (Specie specie : Specie.values()) {
            assertTrue(species.contains(specie.name()));
        }
    }

    @Test
    void shouldReturnAllSizesNames() {
        final var sizes = petService.findSizes();

        assertEquals(Size.values().length, sizes.size());

        for (Size size : Size.values()) {
            assertTrue(sizes.contains(size.name()));
        }
    }

    @Test
    void shouldSavePetFromDTO() {
        final var inputDTO = PetFactory.createDefaultPetInputDTO();
        final var pet = new Pet();

        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        final var savedPet = petService.save(inputDTO);

        assertNotNull(savedPet);
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void shouldSavePetDirectly() {
        final var pet = new Pet();

        when(petRepository.save(pet)).thenReturn(pet);

        final var savedPet = petService.save(pet);

        assertNotNull(savedPet);
        assertEquals(pet, savedPet);
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void shouldUpdatePetWhenExists() {
        final var id = UUID.randomUUID();
        final var existingPet = PetFactory.createDefaultPet();
        existingPet.setId(id);

        final var petInputDTO = PetFactory.createDefaultPetInputDTO();

        doReturn(existingPet).when(petService).findByIdOrThrow(id);
        when(petRepository.save(existingPet)).thenReturn(existingPet);

        final var result = petService.update(id, petInputDTO);

        assertEquals(petInputDTO.name(), result.getName());
        assertEquals(petInputDTO.description(), result.getDescription());
        assertEquals(Specie.valueOf(petInputDTO.specie()), result.getSpecie());
        assertEquals(petInputDTO.breed(), result.getBreed());
        assertEquals(Size.valueOf(petInputDTO.size()), result.getSize());
        assertEquals(Gender.valueOf(petInputDTO.gender()), result.getGender());
        assertEquals(petInputDTO.birthDate(), result.getBirthDate());
        verify(petRepository, times(1)).save(existingPet);
    }

    @Test
    void shouldAdoptPetSuccessfully() {
        final var user = new UserDTO(UUID.randomUUID(), "João", "joao@email.com");
        final var petId = UUID.randomUUID();
        final var pet = PetFactory.createDefaultPet();
        pet.setId(petId);

        when(petRepository.findById(eq(petId))).thenReturn(Optional.of(pet));
        when(petRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final var adoptedPet = petService.adopt(petId, user);

        assertEquals(user.id(), adoptedPet.getOwnerId());
        assertEquals(Status.ADOPTED, adoptedPet.getStatus());
        verify(petRepository).findById(petId);
        verify(petRepository).save(any(Pet.class));
        verify(notificationService).sendAdoptionNotification(any(AdoptionMessage.class));
    }

    @Test
    void shouldThrowWhenPetAlreadyAdopted() {
        final var user = new UserDTO(UUID.randomUUID(), "João", "joao@email.com");
        final var adoptedPet = PetFactory.createAdoptedPet(UUID.randomUUID());

        doReturn(adoptedPet).when(petService).findByIdOrThrow(adoptedPet.getId());

        final var exception = assertThrows(PetAlreadyAdoptedException.class, () ->
                petService.adopt(adoptedPet.getId(), user));

        assertEquals("Pet already adopted.", exception.getMessage());
        verify(petRepository, never()).save(any(Pet.class));
        verify(notificationService, never()).sendAdoptionNotification(any(AdoptionMessage.class));
    }

    @Test
    void shouldThrowWhenStatusIsAdoptedButOwnerIsNull() {
        final var user = new UserDTO(UUID.randomUUID(), "João", "joao@email.com");
        final var adoptedPet = PetFactory.createDefaultPet();
        adoptedPet.setStatus(Status.ADOPTED);
        adoptedPet.setOwnerId(null);

        doReturn(adoptedPet).when(petService).findByIdOrThrow(adoptedPet.getId());

        final var exception = assertThrows(PetAlreadyAdoptedException.class, () ->
                petService.adopt(adoptedPet.getId(), user));

        assertEquals("Pet already adopted.", exception.getMessage());
        verify(petRepository, never()).save(any(Pet.class));
        verify(notificationService, never()).sendAdoptionNotification(any());
    }

    @Test
    void shouldDeletePetById() {
        final var petId = UUID.randomUUID();

        petService.deleteById(petId);

        verify(petRepository, times(1)).deleteById(eq(petId));
    }

}
