package br.com.financialtoolapi.infrastructure.security.services;

import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.ports.out.security.AuthenticationWrapper;
import br.com.financialtoolapi.infrastructure.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalAuthenticationService implements AuthenticationWrapper {

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoggedUserDataDto authenticate(final String username, final String password) {
        final var credentials = new UsernamePasswordAuthenticationToken(username, password);
        final var authenticatedUser = (UserDetailsImpl) authenticationManager
                .authenticate(credentials).getPrincipal();
        return new LoggedUserDataDto(authenticatedUser.getNickname(), authenticatedUser.getEmail());
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
