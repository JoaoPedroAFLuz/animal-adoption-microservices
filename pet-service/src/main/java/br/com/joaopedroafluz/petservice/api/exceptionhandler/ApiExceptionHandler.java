package br.com.joaopedroafluz.petservice.api.exceptionhandler;

import br.com.joaopedroafluz.petservice.domain.exception.BusinessException;
import br.com.joaopedroafluz.petservice.domain.exception.EntityNotFoundException;
import br.com.joaopedroafluz.petservice.domain.exception.UnauthorizedException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex) {
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource not found");

        return problem;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException ex) {
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setTitle("Unauthorized");

        return problem;
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                                                             "You are not authorized to perform this action.");
        problem.setTitle("Access denied");

        return problem;
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ProblemDetail handleOptimisticLockException(OptimisticLockException ex) {
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                                                             "This record was updated by another request. Please try again.");
        problem.setTitle("Conflict");

        return problem;
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex) {
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Business rule violation");

        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUncaught(Exception ex) {
        log.error(ex.getMessage(), ex);

        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                                                             "Internal server error. Try again later.");
        problem.setTitle("Internal server error");

        return problem;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        final var errors = ex.getBindingResult().getFieldErrors().stream()
                             .collect(Collectors.toMap(FieldError::getField,
                                                       e -> messageSource.getMessage(e,
                                                                                     LocaleContextHolder.getLocale()),
                                                       (a, b) -> a));

        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setTitle("Validation error");
        problem.setProperty("errors", errors);

        return ResponseEntity.status(status).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var detail = "Request body is missing or malformed.";

        final var cause = ex.getMostSpecificCause().getMessage();
        if (cause != null && cause.contains("from String")) {
            detail = "Invalid value for enum field: " + extractEnumFieldMessage(cause);
        }

        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Invalid request");

        return ResponseEntity.status(status).body(problem);
    }

    private String extractEnumFieldMessage(String message) {
        final var start = message.indexOf("\"");
        final var end = message.indexOf("\"", start + 1);

        if (start != -1 && end != -1) {
            return message.substring(start + 1, end);
        }

        return "Unknown";
    }

}
