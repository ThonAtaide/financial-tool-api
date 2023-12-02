package br.com.financialtoolapi.application.exceptions;

public class ValidationDataException extends AbstractCustomException {

    public ValidationDataException(String userFriendlyMessage, String developsMessage) {
        super(userFriendlyMessage, developsMessage);
    }
}
