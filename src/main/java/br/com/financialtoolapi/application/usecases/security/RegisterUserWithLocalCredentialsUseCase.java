package br.com.financialtoolapi.application.usecases.security;

import br.com.financialtoolapi.application.exceptions.EntityCreationException;
import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.mapper.UserRegisterMapper;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserWithLocalCredentialsUseCase {

    public static final String USER_ACCOUNT_CREATION_EXCEPTION = "user-account-creation-exception";
    private final UserCredentialDataEntityRepository userCredentialDataEntityRepository;
    private final UserRegisterMapper userRegisterMapper = Mappers.getMapper(UserRegisterMapper.class);
    private final MessageSource messageSource;

    @Transactional
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
            throw new EntityCreationException(
                    InternationalizationUtils.getMessage(messageSource, USER_ACCOUNT_CREATION_EXCEPTION),
                    "User account creation failed during database persistence."
            );
        }

        return new LoggedUserDataDto(userRegisterInputDto.nickname(), userRegisterInputDto.email());
    }

}
