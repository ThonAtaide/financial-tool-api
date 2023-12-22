package br.com.financialtoolapi.application.mapper;

import br.com.financialtoolapi.application.domain.entities.UserCredentialDataEntity;
import br.com.financialtoolapi.application.dtos.out.UserAuthenticationOutputDto;
import br.com.financialtoolapi.application.dtos.out.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper
public interface AuthenticationMapper {

    @Mapping(source = "userAccount.email", target = "email")
    @Mapping(source = "userAccount.nickname", target = "nickname")
    UserDetailsImpl toUserDetailsDto(UserCredentialDataEntity userCredential);
}
