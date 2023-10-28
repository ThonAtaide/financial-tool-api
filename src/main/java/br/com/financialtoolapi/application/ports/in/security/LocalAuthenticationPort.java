package br.com.financialtoolapi.application.ports.in.security;

import br.com.financialtoolapi.application.dtos.out.UserAuthenticationOutputDto;

public interface LocalAuthenticationPort {

    UserAuthenticationOutputDto fetchUserCredentialsByUsername(String username);
}
