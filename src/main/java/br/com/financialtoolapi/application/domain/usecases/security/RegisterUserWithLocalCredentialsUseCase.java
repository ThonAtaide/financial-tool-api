package br.com.financialtoolapi.application.domain.usecases.security;

import br.com.financialtoolapi.application.domain.exceptions.UserPersistenceException;
import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.mapper.UserRegisterMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserWithLocalCredentialsUseCase {

    private final UserCredentialDataEntityRepository userCredentialDataEntityRepository;
    private final UserRegisterMapper userRegisterMapper = Mappers.getMapper(UserRegisterMapper.class);

    public LoggedUserDataDto registerNewUserWithLocalCredentials(
            @NonNull final UserRegisterInputDto userRegisterInputDto
    ) {
        final var userCredentialDataEntity = userRegisterMapper
                .fromUserRegister(userRegisterInputDto);
        try {
            userCredentialDataEntityRepository
                    .save(userCredentialDataEntity);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UserPersistenceException("Houve um erro durante a persistência e o usuário não pôde ser persistido.");
        }

        return new LoggedUserDataDto(userRegisterInputDto.nickname(), userRegisterInputDto.email());
    }

}
