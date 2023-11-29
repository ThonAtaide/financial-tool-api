package br.com.financialtoolapi.api.controller.v1.request;

import jakarta.validation.constraints.NotEmpty;

public record UserRegisterRequestV1(
        @NotEmpty(message = "Username field is required.")
        String username,
        @NotEmpty(message = "Password field is required.")
        String password,
        @NotEmpty(message = "E-mail field is required.")
        String email,
        @NotEmpty(message = "Nickname field is required.")
        String nickname) {
}
