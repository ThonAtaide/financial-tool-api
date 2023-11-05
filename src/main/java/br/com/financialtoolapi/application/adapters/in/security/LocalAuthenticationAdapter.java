package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.domain.usecases.security.RegisterUserWithLocalCredentialsUseCase;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.ports.in.security.LocalAuthenticationPort;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.ports.out.security.AuthenticationWrapper;
import br.com.financialtoolapi.application.validations.useregister.UserRegisterValidation;
import br.com.financialtoolapi.application.dtos.in.UserCredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalAuthenticationAdapter implements LocalAuthenticationPort {

    private final AuthenticationWrapper authenticationWrapper;
    private final List<UserRegisterValidation> userRegisterValidationList;
    private final RegisterUserWithLocalCredentialsUseCase registerUserWithLocalCredentialsUseCase;

    @Override
    public LoggedUserDataDto login(UserCredentialsDto userCredentialsDto) {
        return authenticationWrapper
                .authenticate(userCredentialsDto.username(), userCredentialsDto.password());
    }

    @Override
    public LoggedUserDataDto registerNewUser(UserRegisterInputDto userRegister) {
        userRegisterValidationList
                .forEach(it -> it.validate(userRegister));
        registerUserWithLocalCredentialsUseCase
                .registerNewUserWithLocalCredentials(
                        new UserRegisterInputDto(
                                userRegister.username(),
                                authenticationWrapper.encodePassword(userRegister.password()),
                                userRegister.nickname(),
                                userRegister.email()
                        )
                );
        return this.login(new UserCredentialsDto(userRegister.username(), userRegister.password()));
    }






}
