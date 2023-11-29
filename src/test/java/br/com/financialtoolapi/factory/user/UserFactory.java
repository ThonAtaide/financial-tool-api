package br.com.financialtoolapi.factory.user;

import br.com.financialtoolapi.application.domain.entities.business.UserProfileDataEntity;
import br.com.financialtoolapi.application.domain.entities.security.UserAccountEntity;
import br.com.financialtoolapi.application.domain.entities.security.UserCredentialDataEntity;
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
                .userProfileData(buildUserProfileData())
                .build();
    }
    public UserProfileDataEntity buildUserProfileData() {
        return UserProfileDataEntity
                .builder()
                .nickname(UUID.randomUUID().toString())
                .build();
    }
}


