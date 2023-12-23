package br.com.financialtoolapi.application.adapters.in.business;

import br.com.financialtoolapi.application.domain.entities.UserAccountEntity;
import br.com.financialtoolapi.application.exceptions.EntityCreationException;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.exceptions.UnexpectedInternalErrorException;
import br.com.financialtoolapi.application.ports.out.security.AuthenticationFrameworkWrapper;
import br.com.financialtoolapi.application.usecases.security.FindUserAccountByEmailUseCase;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.ports.in.business.UserAccountManagementPort;
import br.com.financialtoolapi.application.usecases.security.RegisterUserWithLocalCredentialsUseCase;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import br.com.financialtoolapi.application.validations.userinfo.UserInfoValidation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountManagementDataAdapter implements UserAccountManagementPort {

    public static final String USER_NOT_FOUND_BY_EMAIL_TITLE = "user-not-found-by-email-title";
    private final FindUserAccountByEmailUseCase findUserAccountByEmailUseCase;
    private static final String UNEXPECTED_ERROR_MESSAGE = "unexpected.error-title";
    private final AuthenticationFrameworkWrapper authenticationWrapper;
    private final List<UserInfoValidation> userInfoValidationList;
    private final RegisterUserWithLocalCredentialsUseCase registerUserWithLocalCredentialsUseCase;
    private final MessageSource messageSource;

    @Override
    public LoggedUserDataDto createUser(final UserRegisterInputDto userRegister) {
        userInfoValidationList
                .forEach(it -> it.validate(userRegister));
        final String encodedPassword = authenticationWrapper.encodePassword(userRegister.password());

        return registerUserWithLocalCredentialsUseCase
                .registerNewUserWithLocalCredentials(
                        new UserRegisterInputDto(
                                userRegister.username(),
                                encodedPassword,
                                userRegister.nickname(),
                                userRegister.email()
                        )
                );

    }

    @Override
    public UUID fetchUserAccountIdentifierByEmail(@NonNull String email) {
        return findUserAccountByEmailUseCase
                .fetchUserAccountByEmail(email)
                .map(UserAccountEntity::getId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                USER_NOT_FOUND_BY_EMAIL_TITLE,
                                String.format("Could not find an user with this e- mail %s", email))
                );
    }
}
