package br.com.joaopedroafluz.petservice.api.controller;

import br.com.joaopedroafluz.petservice.domain.dto.*;
import br.com.joaopedroafluz.shared.domain.UserDTO;
import br.com.joaopedroafluz.petservice.domain.service.PetService;
import br.com.joaopedroafluz.petservice.util.AuthenticatedUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/pets")
public class PetController {

    private final PetService petService;
    private final AuthenticatedUserUtils authenticatedUserUtils;

    @GetMapping
    public Page<PetResponseDTO> findAll(PetFilter petFilter, @PageableDefault(sort = "createdAt") Pageable pageable) {
        return petService.findAll(petFilter, pageable).map(PetResponseDTO::from);
    }

    @GetMapping("/featured")
    public List<PetResponseDTO> findFeatured() {
        return petService.findFeatured().stream().map(PetResponseDTO::from).toList();
    }

    @GetMapping("/{id}")
    public PetResponseDTO findById(@PathVariable UUID id) {
        return PetResponseDTO.from(petService.findByIdOrThrow(id));
    }

    @GetMapping("/mines")
    public Page<PetResponseDTO> findByLoggedUser(@AuthenticationPrincipal Jwt jwt,
                                                 @PageableDefault(sort = "createdAt") Pageable pageable) {
        String userId = jwt.getSubject();

        return petService.findByOwnerId(UUID.fromString(userId), pageable).map(PetResponseDTO::from);
    }

    @GetMapping("/species")
    public List<String> findSpecies() {
        return petService.findSpecies();
    }

    @GetMapping("/sizes")
    public List<String> findSizes() {
        return petService.findSizes();
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<PetResponseDTO> findByOwner(@PathVariable UUID ownerId,
                                            @PageableDefault(sort = "createdAt") Pageable pageable) {
        return petService.findByOwnerId(ownerId, pageable).map(PetResponseDTO::from);
    }

    @PostMapping
    @PreAuthorize("hasRole('REGISTER_PET')")
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponseDTO save(@RequestBody @Valid PetRegistrationInputDTO petRegistrationInputDTO) {
        return PetResponseDTO.from(petService.save(petRegistrationInputDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('UPDATE_PET')")
    public PetResponseDTO update(@PathVariable UUID id, @RequestBody @Valid PetUpdateInputDTO petUpdateInputDTO) {
        return PetResponseDTO.from(petService.update(id, petUpdateInputDTO));
    }

    @PutMapping("/adopt/{id}")
    public PetResponseDTO adopt(@PathVariable UUID id) {
        final var userId = authenticatedUserUtils.getUserId();
        final var email = authenticatedUserUtils.getEmail();
        final var name = authenticatedUserUtils.getGivenName();

        var user = new UserDTO(UUID.fromString(userId), name, email);

        return PetResponseDTO.from(petService.adopt(id, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DELETE_PET')")
    public void delete(@PathVariable UUID id) {
        petService.deleteById(id);
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasRole('UPDATE_PET')")
    public PetResponseDTO uploadImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        return PetResponseDTO.from(petService.uploadImage(id, file));
    }

}
