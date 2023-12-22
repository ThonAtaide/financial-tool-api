package br.com.financialtoolapi.application.dtos.out;

public record UserAuthenticationOutputDto(String username, String password, String nickname, String email) {
}
