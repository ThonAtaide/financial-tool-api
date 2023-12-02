package br.com.financialtoolapi.api.controller.v1;

import br.com.financialtoolapi.api.controller.v1.response.ErrorResponseV1;
import br.com.financialtoolapi.application.exceptions.UnexpectedInternalErrorException;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.financialtoolapi.api.ErrorType.*;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerV1 {

    @ExceptionHandler(ValidationDataException.class)
    protected ResponseEntity<Object> handleValidationDataException(
            HttpServletRequest request, ValidationDataException ex
    ) {
        final ErrorResponseV1 errorResponse = new ErrorResponseV1(
                ex.getUserFriendlyMessage(),
                PAYLOAD_DATA_VALIDATION_FAIL,
                PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus().value(),
                extractRequestUri(request),
                Instant.now(),
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentials(
            HttpServletRequest request, BadCredentialsException ex
    ) {
        final ErrorResponseV1 errorResponse = new ErrorResponseV1(
                AUTHENTICATION_FAIL_BAD_CREDENTIALS.getTitle(),
                AUTHENTICATION_FAIL_BAD_CREDENTIALS,
                AUTHENTICATION_FAIL_BAD_CREDENTIALS.getHttpStatus().value(),
                extractRequestUri(request),
                Instant.now(),
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(AUTHENTICATION_FAIL_BAD_CREDENTIALS.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            HttpServletRequest request, MethodArgumentNotValidException ex
    ) {
        final List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        final ErrorResponseV1 errorResponse = new ErrorResponseV1(
                PAYLOAD_DATA_VALIDATION_FAIL.getTitle(),
                PAYLOAD_DATA_VALIDATION_FAIL,
                PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus().value(),
                extractRequestUri(request),
                Instant.now(),
                errorMessages
        );
        return ResponseEntity
                .status(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = {UnexpectedInternalErrorException.class})
    protected ResponseEntity<Object> handleUnexpectedException(
            HttpServletRequest request, UnexpectedInternalErrorException ex
    ) {
        final String errorIdentifier = UUID.randomUUID().toString();
        log.error(String.format("Unexpected error %s - ", errorIdentifier).concat(ex.getMessage()));
        final ErrorResponseV1 errorResponse = new ErrorResponseV1(
                ex.getUserFriendlyMessage(),
                UNEXPECTED_INTERNAL_ERROR,
                UNEXPECTED_INTERNAL_ERROR.getHttpStatus().value(),
                extractRequestUri(request),
                Instant.now(),
                List.of("Erro inesperado, contacte o administrador ")
        );
        return ResponseEntity
                .status(UNEXPECTED_INTERNAL_ERROR.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(
            HttpServletRequest request, Exception ex
    ) {
        final String errorIdentifier = UUID.randomUUID().toString();
        log.error(String.format("Unexpected error %s - ", errorIdentifier).concat(ex.getMessage()));
        final ErrorResponseV1 errorResponse = new ErrorResponseV1(
                UNEXPECTED_INTERNAL_ERROR.getTitle(),
                UNEXPECTED_INTERNAL_ERROR,
                UNEXPECTED_INTERNAL_ERROR.getHttpStatus().value(),
                extractRequestUri(request),
                Instant.now(),
                List.of("Erro inesperado, contacte o administrador ")
        );
        return ResponseEntity
                .status(UNEXPECTED_INTERNAL_ERROR.getHttpStatus())
                .body(errorResponse);
    }

    private String extractRequestUri(final HttpServletRequest request) {
        return Optional
                .ofNullable(request)
                .map(HttpServletRequest::getServletPath)
                .orElse("");
    }
}
