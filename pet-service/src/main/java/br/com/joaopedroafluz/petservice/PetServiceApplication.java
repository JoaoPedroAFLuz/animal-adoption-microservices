package br.com.joaopedroafluz.petservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableRabbit
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class PetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetServiceApplication.class, args);
	}

}
