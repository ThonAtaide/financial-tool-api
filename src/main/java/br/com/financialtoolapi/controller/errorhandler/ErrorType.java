package br.com.financialtoolapi.controller.errorhandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    AUTHENTICATION_FAIL_BAD_CREDENTIALS("sign-in.bad-credentials.error-title", UNAUTHORIZED),
    AUTHENTICATION_TOKEN_MISSING("sign-in.access-token-missing.error-title", UNAUTHORIZED),
    PROVIDED_DATA_VALIDATION_FAIL("data-validation.error-title", BAD_REQUEST),
    NOT_FOUND("could-not-find-desired-resource-title", BAD_REQUEST),
    UNEXPECTED_INTERNAL_ERROR("unexpected.error-title", INTERNAL_SERVER_ERROR);

    private final String titleMessageCode;
    private final HttpStatus httpStatus;
}
