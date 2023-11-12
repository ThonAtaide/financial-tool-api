package br.com.financialtoolapi.factory.user;

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
}


