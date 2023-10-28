package br.com.financialtoolapi.application.domain.usecases.security;

import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.application.dtos.out.UserAuthenticationOutputDto;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.mapper.AuthenticationMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserCredentialDataByUsernameUseCase {

    private final AuthenticationMapper authenticationMapper = Mappers.getMapper(AuthenticationMapper.class);
    private final UserCredentialDataEntityRepository userCredentialDataEntityRepository;

    public UserAuthenticationOutputDto findUserCredentialsByUsername(@NonNull String username) {
        return userCredentialDataEntityRepository
                .findUserByUsernameEquals(username)
                .map(authenticationMapper::toUserLoginOutputDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                                String.format("User from username %s was not found", username)
                        )
                );
    }
}
