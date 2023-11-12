package br.com.financialtoolapi.integration.domain.usecase.security;

import br.com.financialtoolapi.application.domain.entities.security.UserCredentialDataEntity;
import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.application.domain.usecases.security.RegisterUserWithLocalCredentialsUseCase;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.factory.user.UserFactory;
import br.com.financialtoolapi.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegisterUserWithLocalCredentialsUseCaseTest extends AbstractIntegrationTest {

    @Autowired
    private RegisterUserWithLocalCredentialsUseCase registerUserWithLocalCredentialsUseCase;

    @Autowired
    private UserCredentialDataEntityRepository userCredentialDataEntityRepository;

    @Test
    void testRegisterUserSuccessfully() {
        final UserRegisterInputDto userRegisterInput = UserFactory.buildWith();
        assertThat(userCredentialDataEntityRepository
                .findUserByUsernameEquals(userRegisterInput.email())
                .isEmpty()
        ).isTrue();

        registerUserWithLocalCredentialsUseCase
                .registerNewUserWithLocalCredentials(userRegisterInput);

        final UserCredentialDataEntity createdUser = userCredentialDataEntityRepository
                .findUserByUsernameEquals(userRegisterInput.username())
                .orElse(null);

        assertThat(createdUser.getUsername()).isEqualTo(userRegisterInput.username());
        assertThat(createdUser.getPassword()).isEqualTo(userRegisterInput.password());
        assertThat(createdUser.getUserAccount().getEmail()).isEqualTo(userRegisterInput.email());
        assertThat(createdUser.getUserAccount().getUserProfileData().getNickname()).isEqualTo(userRegisterInput.nickname());

    }
}
