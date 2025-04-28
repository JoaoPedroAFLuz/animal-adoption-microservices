package br.com.joaopedroafluz.petservice.api.exceptionhandler;

import br.com.joaopedroafluz.petservice.domain.exception.BusinessException;
import br.com.joaopedroafluz.petservice.domain.exception.EntityNotFoundException;
import br.com.joaopedroafluz.petservice.domain.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String GENERIC_MESSAGE = "Internal server error. Try again later";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.BUSINESS_ERROR;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ProblemType problemType = ProblemType.UNAUTHORIZED;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.SYSTEM_ERROR;
        String detail = GENERIC_MESSAGE;

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
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

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail)
                .timestamp(OffsetDateTime.now());
    }

}
