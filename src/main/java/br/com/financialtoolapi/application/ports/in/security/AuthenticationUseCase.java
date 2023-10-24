package br.com.financialtoolapi.application.ports.in.security;

import br.com.financialtoolapi.application.model.dto.out.UserLoginOutputDto;

public interface AuthenticationUseCase {

    UserLoginOutputDto fetchUserCredentialsByUsername(String username);
}
