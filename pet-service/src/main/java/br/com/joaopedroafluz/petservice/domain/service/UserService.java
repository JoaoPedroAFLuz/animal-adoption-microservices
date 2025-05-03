package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.domain.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient.Builder webClient;

    public UserDTO findByAuthorization(String authorization) {
        return webClient.build()
                .get()
                .uri("/auth/me")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .doOnError(e -> log.error("Error fetching user: ", e))
                .block();
    }

}
