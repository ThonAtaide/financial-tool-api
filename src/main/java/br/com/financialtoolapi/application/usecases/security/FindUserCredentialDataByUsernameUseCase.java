package br.com.financialtoolapi.application.usecases.security;

import br.com.financialtoolapi.application.domain.entities.UserCredentialDataEntity;
import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
