package br.com.financialtoolapi.application.domain.usecases.security;

import br.com.financialtoolapi.application.domain.entities.UserAccountEntity;
import br.com.financialtoolapi.application.domain.repositories.UserAccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindUserAccountByEmailUseCase {

    private final UserAccountRepository userAccountRepository;

    public Optional<UserAccountEntity> fetchUserAccountByEmail(@NonNull String email) {
        return userAccountRepository
                .findUserAccountByEmailEquals(email);
    }
}
