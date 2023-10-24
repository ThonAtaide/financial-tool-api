package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.model.entities.UserCredentialEntity;
import br.com.financialtoolapi.application.model.entities.UserProfileEntity;
import br.com.financialtoolapi.application.ports.in.security.FetchUserProfileIdentifierUseCase;
import br.com.financialtoolapi.infrastructure.repository.repositories.UserCredentialEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FetchUserProfileIdentifierUseCaseImpl implements FetchUserProfileIdentifierUseCase {

    private final UserCredentialEntityRepository userCredentialEntityRepository;

    @Override
    public UUID fetchUserProfileIdentifierByUsername(String username) {
        return userCredentialEntityRepository.findUserByUsernameEquals(username)
                .map(UserCredentialEntity::getUserProfile)
                .map(UserProfileEntity::getId)
                .orElseThrow(() -> new ResourceNotFoundException("The username was not found."));
    }
}
