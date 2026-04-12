package br.com.joaopedroafluz.petservice.api.controller;

import br.com.joaopedroafluz.petservice.domain.service.NotificationService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
"spring.cache.type=simple",
"eureka.client.enabled=false",
"spring.rabbitmq.host=localhost",
"spring.rabbitmq.port=0"
})
@AutoConfigureMockMvc
@Testcontainers
class PetControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.17");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    private static final String PET_JSON = """
    {
        "name": "Luna",
        "description": "Friendly dog",
        "specie": "DOG",
        "breed": "Golden Retriever",
        "size": "LARGE",
        "gender": "FEMALE",
        "birthDate": "2023-06-15"
    }
    """;

    @Test
    void shouldCreateAndRetrievePet() throws Exception {
        final var result = mockMvc.perform(post("/pets")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(PET_JSON)
                                           .with(jwt("REGISTER_PET")))
                                  .andExpect(status().isCreated())
                                  .andExpect(jsonPath("$.name").value("Luna"))
                                  .andExpect(jsonPath("$.status").value("AVAILABLE"))
                                  .andExpect(jsonPath("$.version").doesNotExist())
                                  .andReturn();

        final var id = JsonPath.read(result.getResponse().getContentAsString(), "$.id").toString();

        mockMvc.perform(get("/pets/" + id).with(jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Luna"))
               .andExpect(jsonPath("$.specie").value("DOG"));
    }

    @Test
    void shouldReturnFeaturedPets() throws Exception {
        final var result = mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON)
                                                        .content(PET_JSON)
                                                        .with(jwt("REGISTER_PET")))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        final var id = JsonPath.read(result.getResponse().getContentAsString(), "$.id").toString();

        mockMvc.perform(put("/pets/" + id + "/featured").with(jwt("UPDATE_PET")))
               .andExpect(status().isOk());

        mockMvc.perform(get("/pets/featured"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void shouldReturn401WhenCreatingPetWithoutToken() throws Exception {
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PET_JSON))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenCreatingPetWithoutRole() throws Exception {
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PET_JSON)
                        .with(jwt()))
               .andExpect(status().isForbidden());
    }

    @Test
    void shouldUpdatePet() throws Exception {
        final var result = mockMvc.perform(post("/pets")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(PET_JSON)
                                           .with(jwt("REGISTER_PET")))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        final var id = JsonPath.read(result.getResponse().getContentAsString(), "$.id").toString();

        final var updateJson = """
        {
            "name": "Luna Updated",
            "description": "Very friendly dog",
            "specie": "DOG",
            "breed": "Golden Retriever",
            "size": "LARGE",
            "gender": "FEMALE",
            "birthDate": "2023-06-15"
        }
        """;

        mockMvc.perform(put("/pets/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
                        .with(jwt("UPDATE_PET")))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Luna Updated"));
    }

    @Test
    void shouldDeletePet() throws Exception {
        final var result = mockMvc.perform(post("/pets")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(PET_JSON)
                                           .with(jwt("REGISTER_PET")))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        final var id = JsonPath.read(result.getResponse().getContentAsString(), "$.id").toString();

        mockMvc.perform(delete("/pets/" + id)
                        .with(jwt("DELETE_PET")))
               .andExpect(status().isNoContent());

        mockMvc.perform(get("/pets/" + id)
                        .with(jwt()))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldFilterPetsByStatus() throws Exception {
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PET_JSON)
                        .with(jwt("REGISTER_PET")))
               .andExpect(status().isCreated());

        mockMvc.perform(get("/pets?status=AVAILABLE"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));

        mockMvc.perform(get("/pets?status=ADOPTED"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(0)));
    }

    private static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt(String... roles) {
        final var jwtBuilder = SecurityMockMvcRequestPostProcessors.jwt()
                                                                   .jwt(builder -> builder
                                                                   .claim("sub", "test-user-id")
                                                                   .claim("email", "test@email.com")
                                                                   .claim("given_name", "Test User"));

        if (roles.length > 0) {
            final var authorities = Arrays.stream(roles)
                                          .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                                          .toList();

            jwtBuilder.authorities(authorities);
        }

        return jwtBuilder;
    }

}
