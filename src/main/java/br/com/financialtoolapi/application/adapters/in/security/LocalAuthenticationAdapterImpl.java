package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.domain.usecases.security.FindUserCredentialDataByUsernameUseCase;
import br.com.financialtoolapi.application.dtos.out.UserAuthenticationOutputDto;
import br.com.financialtoolapi.application.ports.in.security.LocalAuthenticationPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalAuthenticationAdapterImpl implements LocalAuthenticationPort {

    private final FindUserCredentialDataByUsernameUseCase findUserCredentialDataByUsernameUseCase;

    @Override
    public UserAuthenticationOutputDto fetchUserCredentialsByUsername(@NonNull String username) {
        return findUserCredentialDataByUsernameUseCase
                .findUserCredentialsByUsername(username);
    }
}
