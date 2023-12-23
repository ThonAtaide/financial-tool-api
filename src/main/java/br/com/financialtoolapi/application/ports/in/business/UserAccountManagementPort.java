package br.com.financialtoolapi.application.ports.in.business;

import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;

import java.util.UUID;

public interface UserAccountManagementPort {

    UUID fetchUserAccountIdentifierByEmail(String email);

    LoggedUserDataDto createUser(final UserRegisterInputDto userRegister);
}
