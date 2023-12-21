package br.com.financialtoolapi.api.controller.v1.response;

import br.com.financialtoolapi.api.ErrorType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public record ErrorResponseV1(
        String title,
        List<String> errors,
        ErrorType errorType,
        int statusCode,
        String instance,
        Instant timestamp,
        String developerInfo
        ) {

        public ErrorResponseV1 {
               errors = new ArrayList<>(errors);
        }

    @Override
    public List<String> errors() {
        return new ArrayList<>(errors);
    }
}
