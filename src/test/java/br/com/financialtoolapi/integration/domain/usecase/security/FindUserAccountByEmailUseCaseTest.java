package br.com.financialtoolapi.integration.domain.usecase.security;


import br.com.financialtoolapi.application.domain.entities.business.UserProfileDataEntity;
import br.com.financialtoolapi.application.domain.entities.security.UserAccountEntity;
import br.com.financialtoolapi.application.domain.repositories.UserAccountRepository;
import br.com.financialtoolapi.application.domain.usecases.security.FindUserAccountByEmailUseCase;
import br.com.financialtoolapi.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FindUserAccountByEmailUseCaseTest extends AbstractIntegrationTest {

    @Autowired
    private FindUserAccountByEmailUseCase findUserAccountByEmailUseCase;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    @DisplayName("Given that there is an user account using the email from test " +
            "When findUserAccountByEmailUseCase method is called " +
            "Then it should return the respectively user account wrapped into optional.")
    void testWhenThereIsAnAccountWithEmailRequested() {
        final var createdAccount = populateUserAccountTable();
        final var actual = findUserAccountByEmailUseCase
                .fetchUserAccountByEmail(createdAccount.getEmail())
                .orElse(null);

        assertThat(createdAccount).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    @DisplayName("Given that there is not an user account using the email from test " +
            "When findUserAccountByEmailUseCase method is called " +
            "Then it should return an empty optional.")
    void testWhenThereIsNotAnAccountWithEmailRequested() {
        final var actual = findUserAccountByEmailUseCase
                .fetchUserAccountByEmail(UUID.randomUUID().toString())
                .orElse(null);

        assertThat(actual).isNull();
    }

    private UserAccountEntity populateUserAccountTable() {
        final UserAccountEntity userAccountEntity = UserAccountEntity
                .builder()
                .email(UUID.randomUUID().toString())
                .userProfileData(
                        UserProfileDataEntity
                                .builder()
                                .nickname(UUID.randomUUID().toString())
                                .build()
                ).build();
        return userAccountRepository.save(userAccountEntity);
    }

}
