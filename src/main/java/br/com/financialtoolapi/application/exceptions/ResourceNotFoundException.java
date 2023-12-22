package br.com.financialtoolapi.application.exceptions;

public class ResourceNotFoundException extends AbstractCustomException {

    public ResourceNotFoundException(String userFriendlyMessage, String developsMessage) {
        super(userFriendlyMessage, developsMessage);
    }
}
