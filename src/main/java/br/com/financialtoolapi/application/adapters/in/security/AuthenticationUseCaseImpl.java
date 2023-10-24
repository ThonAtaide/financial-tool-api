package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.mapper.AuthenticationMapper;
import br.com.financialtoolapi.application.model.dto.out.UserLoginOutputDto;
import br.com.financialtoolapi.application.ports.in.security.AuthenticationUseCase;
import br.com.financialtoolapi.infrastructure.repository.repositories.UserCredentialEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCaseImpl implements AuthenticationUseCase {

    private final AuthenticationMapper authenticationMapper = Mappers.getMapper(AuthenticationMapper.class);
    private final UserCredentialEntityRepository userCredentialEntityRepository;

    @Override
    public UserLoginOutputDto fetchUserCredentialsByUsername(@NonNull String username) {
        return userCredentialEntityRepository
                .findUserByUsernameEquals(username)
                .map(authenticationMapper::toUserLoginOutputDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                                String.format("User from username %s was not found", username)
                        )
                );
    }
}
