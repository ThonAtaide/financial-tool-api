package br.com.financialtoolapi.application.exceptions;

public class ResourceCreationException extends RuntimeException {

    public ResourceCreationException(String message) {
        super(message);
    }
}
