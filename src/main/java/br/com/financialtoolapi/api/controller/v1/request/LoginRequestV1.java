package br.com.financialtoolapi.api.controller.v1.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestV1(
        @NotEmpty(message = "Username is required") String username,
        @NotEmpty(message = "Password is required") String password
) {
}
