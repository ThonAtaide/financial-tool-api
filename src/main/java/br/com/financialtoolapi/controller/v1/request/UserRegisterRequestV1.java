package br.com.financialtoolapi.controller.v1.request;

import br.com.financialtoolapi.infrastructure.validations.annotations.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestV1(
        @Size.List({
                @Size(min = 5, message = "{sign-up.username.too-short}"),
                @Size(max = 36, message = "{sign-up.username.too-long}")
        })
        @NotBlank(message = "{sign-up.username.too-short}")
        String username,

        @Size.List({
                @Size(min = 6, message = "{sign-up.password.too-short}"),
                @Size(max = 36, message = "{sign-up.password.too-long}")
        })
        @NotBlank(message = "{sign-up.password.too-short}")
        String password,

//        @Email
        @NotBlank(message = "{sign-up.email.not-blank}")
        String email,

        @Size.List({
                @Size(min = 2, message = "{sign-up.nickname.too-short}"),
                @Size(max = 10, message = "{sign-up.nickname.too-long}")
        })
        @NotBlank(message = "{sign-up.nickname.too-short}")
        String nickname) {
}
