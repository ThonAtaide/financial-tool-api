package br.com.financialtoolapi.application.mapper;

import br.com.financialtoolapi.application.domain.entities.UserAccountEntity;
import br.com.financialtoolapi.application.domain.entities.UserCredentialDataEntity;
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
                .nickname(userRegisterInputDto.nickname())
                .build();
    }
}
