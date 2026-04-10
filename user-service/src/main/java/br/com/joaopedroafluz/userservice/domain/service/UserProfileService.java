package br.com.joaopedroafluz.userservice.domain.service;

import br.com.joaopedroafluz.userservice.domain.dto.ProfileResponseDTO;
import br.com.joaopedroafluz.userservice.domain.dto.UpdatePasswordDTO;
import br.com.joaopedroafluz.userservice.domain.dto.UpdateProfileDTO;

public interface UserProfileService {

    ProfileResponseDTO getProfile(String userId);

    ProfileResponseDTO updateProfile(String userId, UpdateProfileDTO dto);

    void updatePassword(String userId, UpdatePasswordDTO dto);

    ProfileResponseDTO updatePhoto(String userId, String photoUrl);

}
