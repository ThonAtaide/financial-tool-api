package br.com.financialtoolapi.application.ports.in.security;

import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.dtos.in.UserCredentialsDto;

public interface LocalAuthenticationPort {

    LoggedUserDataDto login(final UserCredentialsDto userCredentialsDto);

    LoggedUserDataDto registerNewUser(final UserRegisterInputDto userRegister);
}
