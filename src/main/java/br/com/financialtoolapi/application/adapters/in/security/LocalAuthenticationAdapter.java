package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.dtos.in.UserCredentialsDto;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.ports.in.security.LocalAuthenticationPort;
import br.com.financialtoolapi.application.ports.out.security.AuthenticationFrameworkWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalAuthenticationAdapter implements LocalAuthenticationPort {

    private final AuthenticationFrameworkWrapper authenticationFrameworkWrapper;

    @Override
    public LoggedUserDataDto login(UserCredentialsDto userCredentialsDto) {
        return authenticationFrameworkWrapper
                .authenticate(userCredentialsDto.username(), userCredentialsDto.password());
    }
}
