package br.com.financialtoolapi.integration.domain.usecase.security;

import br.com.financialtoolapi.application.domain.entities.UserAccountEntity;
import br.com.financialtoolapi.application.domain.entities.UserCredentialDataEntity;
import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.application.usecases.security.FindUserCredentialDataByUsernameUseCase;
import br.com.financialtoolapi.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FindUserCredentialDataByUsernameUseCaseTest extends AbstractIntegrationTest {

    @Autowired
    private FindUserCredentialDataByUsernameUseCase findUserCredentialDataByUsernameUseCase;

    @Autowired
    private UserCredentialDataEntityRepository userCredentialDataEntityRepository;

    @Test
    @DisplayName("Given that there is a user credentials with the test username " +
            "When findUserCredentialDataByUsernameUseCase method is called " +
            "Then it should return the respectively user credentials data wrapped into optional.")
    void testWhenThereIsAnAccountWithEmailRequested() {
        final var userCredentialData = populateUserCredentialsTable();
        final var actual = findUserCredentialDataByUsernameUseCase
                .findUserCredentialsByUsername(userCredentialData.getUsername())
                .orElse(null);

        assertThat(userCredentialData).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    @DisplayName("Given that there is not an user the username from test " +
            "When findUserCredentialsByUsername method is called " +
            "Then it should return an empty optional.")
    void testWhenThereIsNotAnAccountWithEmailRequested() {
        final var actual = findUserCredentialDataByUsernameUseCase
                .findUserCredentialsByUsername(UUID.randomUUID().toString())
                .orElse(null);

        assertThat(actual).isNull();
    }

    private UserCredentialDataEntity populateUserCredentialsTable() {
        final UserCredentialDataEntity userCredentialData = UserCredentialDataEntity
                .builder()
                .username(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .userAccount(
                        UserAccountEntity
                                .builder()
                                .email(UUID.randomUUID().toString())
                                .nickname(UUID.randomUUID().toString())
                                .build()
                ).build();
        return userCredentialDataEntityRepository.save(userCredentialData);
    }
}
