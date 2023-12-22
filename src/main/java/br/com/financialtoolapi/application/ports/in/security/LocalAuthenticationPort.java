package br.com.financialtoolapi.application.ports.in.security;

import br.com.financialtoolapi.application.dtos.in.UserCredentialsDto;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;

public interface LocalAuthenticationPort {

    LoggedUserDataDto login(final UserCredentialsDto userCredentialsDto);
}
