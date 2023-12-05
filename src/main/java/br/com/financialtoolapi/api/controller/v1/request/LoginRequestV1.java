package br.com.financialtoolapi.api.controller.v1.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestV1(
        @NotEmpty(message = "{sign-in.username.notblank}") String username,
        @NotEmpty(message = "{sign-in.password.notblank}") String password
) {
}
