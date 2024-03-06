package br.com.financialtoolapi.application.exceptions;

public class InvalidQueryParamFormatException extends ValidationDataException {

    public InvalidQueryParamFormatException(String userFriendlyMessage, String developsMessage) {
        super(userFriendlyMessage, developsMessage);
    }
}
