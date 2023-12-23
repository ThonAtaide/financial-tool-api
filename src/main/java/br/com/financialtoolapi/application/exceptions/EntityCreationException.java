package br.com.financialtoolapi.application.exceptions;

public class EntityCreationException extends UnexpectedInternalErrorException {


    public EntityCreationException(String userFriendlyMessage, String developsMessage) {
        super(userFriendlyMessage, developsMessage);
    }
}
