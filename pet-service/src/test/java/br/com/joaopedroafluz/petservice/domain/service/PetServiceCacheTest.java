package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.domain.repository.PetRepository;
import br.com.joaopedroafluz.petservice.factory.PetFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.cache.type=simple",
        "spring.flyway.enabled=false",
        "eureka.client.enabled=false",
        "spring.rabbitmq.host=localhost",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/test"
})
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        RedisAutoConfiguration.class
})
class PetServiceCacheTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private PetService petService;

    @MockitoBean
    private PetRepository petRepository;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void shouldCacheFeaturedPets() {
        var pets = List.of(PetFactory.createDefaultPet());
        when(petRepository.findFeatured(any())).thenReturn(pets);

        petService.findFeatured();
        petService.findFeatured();

        verify(petRepository, times(1)).findFeatured(any());
    }

}
