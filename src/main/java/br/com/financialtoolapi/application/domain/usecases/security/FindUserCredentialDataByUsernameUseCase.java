package br.com.financialtoolapi.application.domain.usecases.security;

import br.com.financialtoolapi.application.domain.entities.security.UserCredentialDataEntity;
import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.application.mapper.AuthenticationMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindUserCredentialDataByUsernameUseCase {

    private final UserCredentialDataEntityRepository userCredentialDataEntityRepository;

    public Optional<UserCredentialDataEntity> findUserCredentialsByUsername(@NonNull String username) {
        return userCredentialDataEntityRepository
                .findUserByUsernameEquals(username);
    }
}
