package br.com.financialtoolapi.application.mapper;

import br.com.financialtoolapi.application.domain.entities.UserCredentialDataEntity;
import br.com.financialtoolapi.application.dtos.out.UserAuthenticationOutputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthenticationMapper {

    @Mapping(source = "userAccount.email", target = "email")
    @Mapping(source = "userAccount.nickname", target = "nickname")
    UserAuthenticationOutputDto toUserLoginOutputDto(UserCredentialDataEntity userCredential);
}
