package br.com.financialtoolapi.controller.v1.mapper;

import br.com.financialtoolapi.controller.v1.request.LoginRequestV1;
import br.com.financialtoolapi.controller.v1.request.UserRegisterRequestV1;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.dtos.in.UserCredentialsDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserDataMapper {

    UserCredentialsDto from(LoginRequestV1 loginRequest);

    UserRegisterInputDto from(UserRegisterRequestV1 userRegisterRequest);
}
