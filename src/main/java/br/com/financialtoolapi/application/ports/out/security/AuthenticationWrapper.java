package br.com.financialtoolapi.application.ports.out.security;

import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;

public interface AuthenticationWrapper {

    String encodePassword(String password);

    LoggedUserDataDto authenticate(String username, String password);
}
