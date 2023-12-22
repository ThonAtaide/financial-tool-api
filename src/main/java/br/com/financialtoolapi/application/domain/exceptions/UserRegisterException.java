package br.com.financialtoolapi.application.domain.exceptions;

public class UserRegisterException extends RuntimeException {

    public UserRegisterException(String message) {
        super(message);
    }
}
