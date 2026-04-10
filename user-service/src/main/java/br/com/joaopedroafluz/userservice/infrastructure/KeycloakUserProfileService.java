package br.com.joaopedroafluz.userservice.infrastructure;

import br.com.joaopedroafluz.userservice.config.KeycloakProperties;
import br.com.joaopedroafluz.userservice.domain.dto.ProfileResponseDTO;
import br.com.joaopedroafluz.userservice.domain.dto.UpdatePasswordDTO;
import br.com.joaopedroafluz.userservice.domain.dto.UpdateProfileDTO;
import br.com.joaopedroafluz.userservice.domain.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakUserProfileService implements UserProfileService {

    private final RestClient restClient;
    private final KeycloakProperties keycloakProperties;

    @Override
    public ProfileResponseDTO getProfile(String userId) {
        final var user = getUser(userId);

        return toProfileResponse(user);
    }

    @Override
    public ProfileResponseDTO updateProfile(String userId, UpdateProfileDTO dto) {
        final var body = Map.of(
                "firstName", dto.firstName(),
                "lastName", dto.lastName()
        );

        restClient.put()
                .uri(getUserUri(userId))
                .headers(h -> h.setBearerAuth(getServiceAccountToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        return getProfile(userId);
    }

    @Override
    public void updatePassword(String userId, UpdatePasswordDTO dto) {
        verifyCurrentPassword(userId, dto.currentPassword());

        final var credentials = Map.of(
                "type", "password",
                "value", dto.newPassword(),
                "temporary", false
        );

        restClient.put()
                .uri(getUserUri(userId) + "/reset-password")
                .headers(h -> h.setBearerAuth(getServiceAccountToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(credentials)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public ProfileResponseDTO updatePhoto(String userId, String photoUrl) {
        final var user = getUser(userId);

        @SuppressWarnings("unchecked")
        final var attributes = (Map<String, Object>) user.getOrDefault("attributes", new java.util.HashMap<>());
        attributes.put("picture", List.of(photoUrl));

        final var body = Map.of("attributes", attributes);

        restClient.put()
                .uri(getUserUri(userId))
                .headers(h -> h.setBearerAuth(getServiceAccountToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        return getProfile(userId);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getUser(String userId) {
        return restClient.get()
                .uri(getUserUri(userId))
                .headers(h -> h.setBearerAuth(getServiceAccountToken()))
                .retrieve()
                .body(Map.class);
    }

    private void verifyCurrentPassword(String userId, String currentPassword) {
        final var user = getUser(userId);
        final var username = (String) user.get("username");

        final var formData = new LinkedMultiValueMap<String, String>();
        formData.add("grant_type", "password");
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());
        formData.add("username", username);
        formData.add("password", currentPassword);

        restClient.post()
                .uri(keycloakProperties.getServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }

    @SuppressWarnings("unchecked")
    private String getServiceAccountToken() {
        final var formData = new LinkedMultiValueMap<String, String>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());

        final var response = restClient.post()
                .uri(keycloakProperties.getServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(Map.class);

        return (String) response.get("access_token");
    }

    private String getUserUri(String userId) {
        return keycloakProperties.getServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users/" + userId;
    }

    @SuppressWarnings("unchecked")
    private ProfileResponseDTO toProfileResponse(Map<String, Object> user) {
        final var attributes = (Map<String, List<String>>) user.getOrDefault("attributes", Map.of());
        final var pictureList = attributes.getOrDefault("picture", List.of());
        final var picture = pictureList.isEmpty() ? null : pictureList.getFirst();

        return new ProfileResponseDTO(
                (String) user.get("id"),
                (String) user.get("firstName"),
                (String) user.get("lastName"),
                (String) user.get("email"),
                picture
        );
    }

}
