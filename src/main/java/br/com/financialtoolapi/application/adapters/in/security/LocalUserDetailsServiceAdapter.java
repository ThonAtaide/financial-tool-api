package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.domain.usecases.security.FindUserCredentialDataByUsernameUseCase;
import br.com.financialtoolapi.application.dtos.out.UserAuthenticationOutputDto;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.mapper.AuthenticationMapper;
import br.com.financialtoolapi.application.ports.in.security.LocalUserDetailsServicePort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalUserDetailsServiceAdapter implements LocalUserDetailsServicePort {

    private final AuthenticationMapper authenticationMapper = Mappers.getMapper(AuthenticationMapper.class);
    private final FindUserCredentialDataByUsernameUseCase findUserCredentialDataByUsernameUseCase;

    @Override
    public UserAuthenticationOutputDto fetchUserCredentialsByUsername(@NonNull String username) {
        return findUserCredentialDataByUsernameUseCase
                .findUserCredentialsByUsername(username)
                .map(authenticationMapper::toUserLoginOutputDto)
                .orElseThrow(() -> new UsernameNotFoundException(
                                String.format("User from username %s was not found", username)
                        )
                );
    }
}
