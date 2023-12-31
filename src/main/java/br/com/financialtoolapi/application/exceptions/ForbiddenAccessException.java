package br.com.financialtoolapi.application.exceptions;

public class ForbiddenAccessException extends AbstractCustomException {

    public ForbiddenAccessException(String userFriendlyMessage, String developsMessage) {
        super(userFriendlyMessage, developsMessage);
    }
}
