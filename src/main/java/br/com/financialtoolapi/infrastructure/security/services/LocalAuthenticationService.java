package br.com.financialtoolapi.infrastructure.security.services;

import br.com.financialtoolapi.application.adapters.in.security.UserAccountDataAdapter;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.ports.out.security.AuthenticationWrapper;
import br.com.financialtoolapi.infrastructure.config.properties.JwtProperties;
import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.infrastructure.security.dto.UserCredentialsDto;
import br.com.financialtoolapi.infrastructure.security.dto.UserDetailsImpl;
import br.com.financialtoolapi.infrastructure.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalAuthenticationService implements AuthenticationWrapper {

    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoggedUserDataDto authenticate(String username, String password) {
        final var credentials = new UsernamePasswordAuthenticationToken(username, password);
        final var authenticatedUser = (UserDetailsImpl) authenticationManager
                .authenticate(credentials).getPrincipal();
        final String jwtToken = JwtUtils
                .buildJwtToken(
                        authenticatedUser.getEmail(),
                        jwtEncoder,
                        jwtProperties.getTokenDurationSeconds()
                );
        return new LoggedUserDataDto(jwtToken, authenticatedUser.getNickname());
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}
