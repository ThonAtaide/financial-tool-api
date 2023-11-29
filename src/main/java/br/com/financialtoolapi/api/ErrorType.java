package br.com.financialtoolapi.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    AUTHENTICATION_FAIL_BAD_CREDENTIALS("Usuário ou senha incorretos.", UNAUTHORIZED),
    AUTHENTICATION_FAIL_EXPIRED_CREDENTIALS("Sessão expirada.", UNAUTHORIZED),
    PAYLOAD_DATA_VALIDATION_FAIL("Informações inválidas ou incompletas", BAD_REQUEST),
    UNEXPECTED_INTERNAL_ERROR("Houve um erro inesperado. Por favor, tente novamente.", INTERNAL_SERVER_ERROR);

    private final String title;
    private final HttpStatus httpStatus;
}
