package br.com.financialtoolapi.application.domain.usecases.security;

import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.dtos.out.UserRegisterOutputDto;
import br.com.financialtoolapi.application.mapper.UserRegisterMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserWithLocalCredentialsUseCase {

    private final UserCredentialDataEntityRepository userCredentialDataEntityRepository;
    private final UserRegisterMapper userRegisterMapper = Mappers.getMapper(UserRegisterMapper.class);

    public UserRegisterOutputDto registerNewUserWithLocalCredentials(
            @NonNull final UserRegisterInputDto userRegisterInputDto
    ) {
        Optional
                .of(userRegisterInputDto)
                .map(userRegisterMapper::fromUserRegister)
                .map(userCredentialDataEntityRepository::save)
                .ifPresent(it -> log.info("Usuário criado com sucesso {}", it));


        return null;
    }

}
