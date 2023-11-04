package br.com.financialtoolapi.application.mapper;

import br.com.financialtoolapi.application.domain.entities.business.UserProfileDataEntity;
import br.com.financialtoolapi.application.domain.entities.security.UserAccountEntity;
import br.com.financialtoolapi.application.domain.entities.security.UserCredentialDataEntity;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRegisterMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "userRegisterInputDto", target = "userAccount", qualifiedByName = "mapAccountEntity")
    UserCredentialDataEntity fromUserRegister(UserRegisterInputDto userRegisterInputDto);

    @Named("mapAccountEntity")
    default UserAccountEntity mapAccountEntity(final UserRegisterInputDto userRegisterInputDto) {
        return UserAccountEntity
                .builder()
                .email(userRegisterInputDto.email())
                .userProfileData(mapUserProfileData(userRegisterInputDto.nickname()))
                .build();
    }

    default UserProfileDataEntity mapUserProfileData(final String nickname) {
        return UserProfileDataEntity
                .builder()
                .nickname(nickname)
                .build();

    }
}
