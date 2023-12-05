package br.com.financialtoolapi.api.controller.v1.response;

import br.com.financialtoolapi.api.ErrorType;

import java.time.Instant;
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
}
