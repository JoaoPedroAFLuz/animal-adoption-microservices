package br.com.joaopedroafluz.userservice.api.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ProblemDetail handleKeycloakError(HttpClientErrorException ex) {
        log.error("Keycloak API error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());

        if (ex.getStatusCode().value() == 401) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid current password");
        }

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, "Failed to communicate with identity provider");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationError(MethodArgumentNotValidException ex) {
        final var errors = ex.getBindingResult().getFieldErrors().stream()
                             .collect(Collectors.toMap(FieldError::getField,
                                                       e -> e.getDefaultMessage() != null ?
                                                               e.getDefaultMessage() : "Invalid value",
                                                       (a, b) -> a));

        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setProperty("errors", errors);

        return problem;
    }

}
