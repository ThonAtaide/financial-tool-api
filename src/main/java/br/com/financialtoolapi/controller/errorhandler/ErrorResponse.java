package br.com.financialtoolapi.controller.errorhandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public record ErrorResponse(
        String title,
        List<String> errors,
        ErrorType errorType,
        int statusCode,
        String instance,
        Instant timestamp,
        String developerInfo
        ) {

        public ErrorResponse {
               errors = new ArrayList<>(errors);
        }

    @Override
    public List<String> errors() {
        return new ArrayList<>(errors);
    }
}
