package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.domain.entities.security.UserAccountEntity;
import br.com.financialtoolapi.application.domain.repositories.UserAccountRepository;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.ports.in.security.UserAccountPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountDataAdapter implements UserAccountPort {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UUID fetchUserAccountIdentifierByEmail(@NonNull String email) {
        return userAccountRepository
                .findUserAccountByEmailEquals(email)
                .map(UserAccountEntity::getId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("Não foi possível encontrar uma conta vinculada ao email %s", email))
                );
    }
}
