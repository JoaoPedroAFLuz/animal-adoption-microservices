package br.com.joaopedroafluz.userservice.api.controller;

import br.com.joaopedroafluz.userservice.config.MinioProperties;
import br.com.joaopedroafluz.userservice.domain.dto.ProfileResponseDTO;
import br.com.joaopedroafluz.userservice.domain.dto.UpdatePasswordDTO;
import br.com.joaopedroafluz.userservice.domain.dto.UpdateProfileDTO;
import br.com.joaopedroafluz.userservice.domain.service.ImageStorageService;
import br.com.joaopedroafluz.userservice.domain.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;
    private final ImageStorageService imageStorageService;
    private final MinioProperties minioProperties;

    @GetMapping
    public ProfileResponseDTO getProfile(JwtAuthenticationToken authentication) {
        return userProfileService.getProfile(authentication.getName());
    }

    @PutMapping
    public ProfileResponseDTO updateProfile(@Valid @RequestBody UpdateProfileDTO dto,
                                            JwtAuthenticationToken authentication) {
        return userProfileService.updateProfile(authentication.getName(), dto);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto,
                                               JwtAuthenticationToken authentication) {
        userProfileService.updatePassword(authentication.getName(), dto);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/photo")
    public ProfileResponseDTO updatePhoto(@RequestParam("file") MultipartFile file,
                                          JwtAuthenticationToken authentication) {
        final var currentProfile = userProfileService.getProfile(authentication.getName());

        if (currentProfile.picture() != null && currentProfile.picture().startsWith(minioProperties.getEndpoint())) {
            imageStorageService.delete(currentProfile.picture());
        }

        final var photoUrl = imageStorageService.upload(file);

        return userProfileService.updatePhoto(authentication.getName(), photoUrl);
    }

}
