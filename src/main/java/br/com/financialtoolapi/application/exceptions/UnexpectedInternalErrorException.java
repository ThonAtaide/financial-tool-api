package br.com.financialtoolapi.application.exceptions;

public class UnexpectedInternalErrorException extends AbstractCustomException {

    public UnexpectedInternalErrorException(String userFriendlyMessage, String developsMessage) {
        super(userFriendlyMessage, developsMessage);
    }
}
