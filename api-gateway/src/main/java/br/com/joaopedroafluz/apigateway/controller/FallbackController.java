package br.com.joaopedroafluz.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping(value = "/fallback", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public Mono<ProblemDetail> fallback(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);

        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE,
                                                             "Service is temporarily unavailable. Please try again later.");
        problem.setTitle("Service unavailable");

        return Mono.just(problem);
    }

}
