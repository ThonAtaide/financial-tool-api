package br.com.financialtoolapi.api.controller.v1;

import br.com.financialtoolapi.api.ErrorType;
import br.com.financialtoolapi.api.controller.v1.response.ErrorResponseV1;
import br.com.financialtoolapi.application.exceptions.UnexpectedInternalErrorException;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
@RequiredArgsConstructor
public class ExceptionHandlerV1 {

    public static final String UNIDENTIFIED_ERROR_DEVELOPER_MESSAGE = "Unexpected error, contact administrator with correlation id %s";
    public static final String ARGUMENT_NOT_VALID_EXCEPTION_DEVELOPER_MESSAGE = "Payload didn't attend the expectations and request couldn't be reached.";
    private final MessageSource messageSource;

    @ExceptionHandler(ValidationDataException.class)
    protected ResponseEntity<Object> handleValidationDataException(
            final HttpServletRequest request, final ValidationDataException ex
    ) {
        final ErrorResponseV1 errorResponse = buildErrorResponse(
                PAYLOAD_DATA_VALIDATION_FAIL,
                request,
                List.of(ex.getUserFriendlyMessage()),
                ex.getMessage()
        );
        return ResponseEntity
                .status(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentials(
            final HttpServletRequest request, final BadCredentialsException ex
    ) {
        final ErrorResponseV1 errorResponse = buildErrorResponse(
                AUTHENTICATION_FAIL_BAD_CREDENTIALS,
                request,
                List.of(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(AUTHENTICATION_FAIL_BAD_CREDENTIALS.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final HttpServletRequest request, final MethodArgumentNotValidException ex
    ) {
        final List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        final ErrorResponseV1 errorResponse = buildErrorResponse(
                PAYLOAD_DATA_VALIDATION_FAIL,
                request,
                errorMessages,
                ARGUMENT_NOT_VALID_EXCEPTION_DEVELOPER_MESSAGE
        );
        return ResponseEntity
                .status(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = {UnexpectedInternalErrorException.class})
    protected ResponseEntity<Object> handleUnexpectedException(
            final HttpServletRequest request, final UnexpectedInternalErrorException ex
    ) {
        final String errorIdentifier = UUID.randomUUID().toString();
        log.error(String.format("Unexpected error %s - ", errorIdentifier).concat(ex.getMessage()));

        final ErrorResponseV1 errorResponse = buildErrorResponse(
                UNEXPECTED_INTERNAL_ERROR,
                request,
                List.of(ex.getUserFriendlyMessage()),
                String.format(UNIDENTIFIED_ERROR_DEVELOPER_MESSAGE, errorIdentifier)
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

        final ErrorResponseV1 errorResponse = buildErrorResponse(
                UNEXPECTED_INTERNAL_ERROR,
                request,
                List.of(),
                String.format(UNIDENTIFIED_ERROR_DEVELOPER_MESSAGE, errorIdentifier)
        );
        return ResponseEntity
                .status(UNEXPECTED_INTERNAL_ERROR.getHttpStatus())
                .body(errorResponse);
    }

    private ErrorResponseV1 buildErrorResponse(
            final ErrorType errorType,
            final HttpServletRequest request,
            final List<String> userFriendlyErrorMessages,
            final String developerMessage
    ) {
        final String title = getErrorMessage(errorType.getTitleMessageCode());
        return new ErrorResponseV1(
                title,
                userFriendlyErrorMessages,
                errorType,
                errorType.getHttpStatus().value(),
                extractRequestUri(request),
                Instant.now(),
                developerMessage
        );
    }

    private String extractRequestUri(final HttpServletRequest request) {
        return Optional
                .ofNullable(request)
                .map(HttpServletRequest::getServletPath)
                .orElse("");
    }

    private String getErrorMessage(final String messageCode, final Object... args) {
        return InternationalizationUtils.getMessage(messageSource, messageCode, args);
    }
}
