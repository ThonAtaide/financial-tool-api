package br.com.financialtoolapi.application.exceptions;

public class UnexpectedInternalErrorException extends RuntimeException {

    public UnexpectedInternalErrorException(String message) {
        super(message);
    }
}
