package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.config.RabbitProperties;
import br.com.joaopedroafluz.petservice.domain.dtos.AdoptionMessage;
import br.com.joaopedroafluz.petservice.domain.dtos.PetInputDTO;
import br.com.joaopedroafluz.petservice.domain.enums.Gender;
import br.com.joaopedroafluz.petservice.domain.enums.Size;
import br.com.joaopedroafluz.petservice.domain.enums.Specie;
import br.com.joaopedroafluz.petservice.domain.enums.Status;
import br.com.joaopedroafluz.petservice.domain.exception.PetAlreadyAdoptedException;
import br.com.joaopedroafluz.petservice.domain.exception.PetNotFoundException;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import br.com.joaopedroafluz.petservice.domain.repository.PetRepository;
import br.com.joaopedroafluz.petservice.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final UserService userService;
    private final PetRepository petRepository;
    private final MessageProducer messageProducer;
    private final RabbitProperties rabbitProperties;

    public Optional<Pet> findById(Long id) {
        return petRepository.findById(id);
    }

    public Pet findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PetNotFoundException(id));
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public List<Pet> findByOwnerId(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public Pet save(PetInputDTO newPetDTO) {
        var pet = convertInputDTOToModel(newPetDTO);

        return save(pet);
    }

    @Transactional
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    @Transactional
    public Pet update(Long id, PetInputDTO petInputDTO) {
        var petFound = findByIdOrThrow(id);

        petFound.setName(petInputDTO.name());
        petFound.setDescription(petInputDTO.description());
        petFound.setSpecie(Specie.valueOf(petInputDTO.specie()));
        petFound.setBreed(petInputDTO.breed());
        petFound.setSize(Size.valueOf(petInputDTO.size()));
        petFound.setStatus(Status.valueOf(petInputDTO.status()));
        petFound.setGender(Gender.valueOf(petInputDTO.gender()));
        petFound.setBirthDate(petInputDTO.birthDate());

        return save(petFound);
    }

    @Transactional
    public Pet adopt(Long petId, String authorization) {
        var pet = findByIdOrThrow(petId);

        if (pet.getOwnerId() != null || Status.ADOPTED.equals(pet.getStatus())) {
            throw new PetAlreadyAdoptedException();
        }

        final var user = userService.findByAuthorization(authorization);

        pet.setOwnerId(user.id());
        pet.setStatus(Status.ADOPTED);

        pet = save(pet);

        sendAdoptionNotification(new AdoptionMessage(pet.getId(), pet.getName(), user.id(), user.name(), user.email()));

        return pet;
    }

    private void sendAdoptionNotification(AdoptionMessage adoptionMessage) {
        var message = new Message(JsonUtils.toJson(adoptionMessage).getBytes());

        messageProducer.sendMessage(rabbitProperties.getExchange(), rabbitProperties.getRoutingKey(), message);
    }

    public void deleteById(Long id) {
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
                .status(Status.AVAILABLE)
                .gender(Gender.valueOf(petInputDTO.gender()))
                .birthDate(petInputDTO.birthDate())
                .build();
    }

}
