package br.com.joaopedroafluz.petservice.api.exceptionhandler;

import br.com.joaopedroafluz.petservice.domain.exception.BusinessException;
import br.com.joaopedroafluz.petservice.domain.exception.EntityNotFoundException;
import br.com.joaopedroafluz.petservice.domain.exception.UnauthorizedException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String GENERIC_MESSAGE = "Internal server error. Try again later";

    private final MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var problemType = ProblemType.BUSINESS_ERROR;
        var detail = ex.getMessage();

        var problem = createProblemBuilder(status, problemType, detail)
                .userMessage(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var problemType = ProblemType.RESOURCE_NOT_FOUND;
        var detail = ex.getMessage();

        var problem = createProblemBuilder(status, problemType, detail)
                .userMessage(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        var status = HttpStatus.UNAUTHORIZED;
        var problemType = ProblemType.UNAUTHORIZED;
        var detail = ex.getMessage();

        var problem = createProblemBuilder(status, problemType, detail)
                .userMessage(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex,
                                                                     WebRequest request) {
        var status = HttpStatus.FORBIDDEN;
        var problemType = ProblemType.ACCESS_DENIED;
        var detail = "You are not authorized to perform this action.";

        var problem = createProblemBuilder(status, problemType, detail)
                .userMessage(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<Object> handleOptimisticLockException(OptimisticLockException ex, WebRequest request) {
        var status = HttpStatus.CONFLICT;
        var problemType = ProblemType.BUSINESS_ERROR;
        var detail = "This record was updated by another request. Please try again.";

        var problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var problemType = ProblemType.SYSTEM_ERROR;
        var detail = GENERIC_MESSAGE;

        var problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var problemType = ProblemType.INVALID_PARAMETER;
        var detail = "One or more parameters are invalid.";

        var problemObjects = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(objectError -> {
                    String name = objectError.getObjectName();
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        var problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               HttpHeaders headers, HttpStatusCode status,
                                                               WebRequest request) {
        var problemType = ProblemType.INVALID_PARAMETER;
        var detail = "One or more parameters are invalid.";

        var cause = ex.getMostSpecificCause().getMessage();
        if (cause != null && cause.contains("from String")) {
            detail = "Invalid value for enum field: " + extractEnumFieldMessage(cause);
        }
        var problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (Objects.isNull(body)) {
            body = Problem.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userMessage(GENERIC_MESSAGE)
                    .timestamp(OffsetDateTime.now())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .userMessage(GENERIC_MESSAGE)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemType, String detail) {
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail)
                .timestamp(OffsetDateTime.now());
    }

    private String extractEnumFieldMessage(String message) {
        // Exemplo de mensagem:
        // Cannot deserialize value of type `Status` from String "INVALID": not one of the values accepted...
        int start = message.indexOf("\"");
        int end = message.indexOf("\"", start + 1);
        if (start != -1 && end != -1) {
            return message.substring(start + 1, end);
        }
        return "Unknown";
    }

}
