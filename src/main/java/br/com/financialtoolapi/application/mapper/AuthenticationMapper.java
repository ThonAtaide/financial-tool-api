package br.com.financialtoolapi.application.mapper;

import br.com.financialtoolapi.application.model.dto.out.UserLoginOutputDto;
import br.com.financialtoolapi.application.model.entities.UserCredentialEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthenticationMapper {

    @Mapping(source = "userProfile.nickname", target = "nickname")
    UserLoginOutputDto toUserLoginOutputDto(UserCredentialEntity userCredential);
}
