package br.com.joaopedroafluz.petservice.api.controller;

import br.com.joaopedroafluz.petservice.config.AuthenticatedUserUtils;
import br.com.joaopedroafluz.petservice.domain.dto.PetInputDTO;
import br.com.joaopedroafluz.petservice.domain.dto.UserDTO;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import br.com.joaopedroafluz.petservice.domain.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/pets")
public class PetController {

    private final PetService petService;
    private final AuthenticatedUserUtils authenticatedUserUtils;

    @GetMapping
    public List<Pet> findAll() {
        return petService.findAll();
    }

    @GetMapping("/{id}")
    public Pet findById(@PathVariable UUID id) {
        return petService.findByIdOrThrow(id);
    }

    @GetMapping("/mines")
    public List<Pet> findByLoggedUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        return petService.findByOwnerId(UUID.fromString(userId));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Pet> findByOwner(@PathVariable UUID ownerId) {
        return petService.findByOwnerId(ownerId);
    }

    @PostMapping
    @PreAuthorize("hasRole('REGISTER_PET')")
    @ResponseStatus(HttpStatus.CREATED)
    public Pet save(@RequestBody PetInputDTO petInputDTO) {
        return petService.save(petInputDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('UPDATE_PET')")
    public Pet update(@PathVariable UUID id, @RequestBody PetInputDTO petInputDTO) {
        return petService.update(id, petInputDTO);
    }

    @PutMapping("/adopt/{id}")
    public Pet adopt(@PathVariable UUID id) {
        final var userId = authenticatedUserUtils.getUserId();
        final var email = authenticatedUserUtils.getEmail();
        final var name = authenticatedUserUtils.getGivenName();

        var user = new UserDTO(UUID.fromString(userId), name, email);

        return petService.adopt(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DELETE_PET')")
    public void delete(@PathVariable UUID id) {
        petService.deleteById(id);
    }

}
