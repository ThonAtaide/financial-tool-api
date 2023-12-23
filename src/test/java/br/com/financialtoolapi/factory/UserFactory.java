package br.com.financialtoolapi.factory;

import br.com.financialtoolapi.application.domain.entities.UserAccountEntity;
import br.com.financialtoolapi.application.domain.entities.UserCredentialDataEntity;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UserFactory {

    public UserRegisterInputDto buildWith() {
        return new UserRegisterInputDto(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );
    }

    public UserCredentialDataEntity buildUserCredentialsEntity(final String encodedPassword) {
        return UserCredentialDataEntity
                .builder()
                .username(UUID.randomUUID().toString())
                .password(encodedPassword)
                .userAccount(buildUserAccountEntity())
                .build();
    }

    public UserAccountEntity buildUserAccountEntity() {
        return UserAccountEntity
                .builder()
                .email(UUID.randomUUID().toString())
                .nickname(UUID.randomUUID().toString())
                .build();
    }
}


