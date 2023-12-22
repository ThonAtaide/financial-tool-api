package br.com.financialtoolapi.application.exceptions;

import lombok.Getter;

@Getter
public abstract class AbstractCustomException extends RuntimeException {

    private final String userFriendlyMessage;

    public AbstractCustomException(String userFriendlyMessage, String developsMessage) {
        super(developsMessage);
        this.userFriendlyMessage = userFriendlyMessage;
    }
}
