package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.domain.exceptions.UserPersistenceException;
import br.com.financialtoolapi.application.domain.usecases.security.RegisterUserWithLocalCredentialsUseCase;
import br.com.financialtoolapi.application.dtos.in.UserCredentialsDto;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.exceptions.ResourceCreationException;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import br.com.financialtoolapi.application.ports.in.security.LocalAuthenticationPort;
import br.com.financialtoolapi.application.ports.out.security.AuthenticationWrapper;
import br.com.financialtoolapi.application.validations.useregister.UserRegisterValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        try {
            userRegisterValidationList
                    .forEach(it -> it.validate(userRegister));
            final String encodePassword = authenticationWrapper.encodePassword(userRegister.password());
            return registerUserWithLocalCredentialsUseCase
                    .registerNewUserWithLocalCredentials(
                            new UserRegisterInputDto(
                                    userRegister.username(),
                                    encodePassword,
                                    userRegister.nickname(),
                                    userRegister.email()
                            )
                    );
        } catch (ValidationDataException ex) {
            log.error(String.valueOf(ex));
            throw new ResourceCreationException(ex.getMessage());
        } catch (UserPersistenceException ex) {
            log.error(String.valueOf(ex));
            throw new ResourceCreationException("Houve um erro inesperado e o usuário não pode ser registrado. Por favor tente novamente.");
        }
    }


}
