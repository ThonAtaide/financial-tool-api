package br.com.financialtoolapi.application.exceptions;

public class ValidationDataException extends RuntimeException {

    public ValidationDataException(String message) {
        super(message);
    }
}
