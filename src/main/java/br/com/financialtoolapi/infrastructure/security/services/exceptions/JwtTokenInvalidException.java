package br.com.financialtoolapi.infrastructure.security.services.exceptions;

public class JwtTokenInvalidException extends RuntimeException {

    public JwtTokenInvalidException(String message) {
        super(message);
    }
}
