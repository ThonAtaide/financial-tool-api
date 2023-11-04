package br.com.financialtoolapi.api.controller.v1.request;

public record UserRegisterRequestV1(String username, String password, String email, String nickname) {
}
