package br.com.financialtoolapi.application.domain.exceptions;

public class UserPersistenceException extends RuntimeException {

    public UserPersistenceException(String message) {
        super(message);
    }
}
