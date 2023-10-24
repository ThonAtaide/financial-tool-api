package br.com.financialtoolapi.infrastructure.security;

import br.com.financialtoolapi.infrastructure.config.properties.JwtProperties;
import br.com.financialtoolapi.infrastructure.security.dto.LoginOutputDataDto;
import br.com.financialtoolapi.infrastructure.security.dto.UserCredentialsDto;
import br.com.financialtoolapi.infrastructure.security.dto.UserDetailsImpl;
import br.com.financialtoolapi.infrastructure.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalAuthenticationService {

    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public LoginOutputDataDto login(final UserCredentialsDto userCredentialsDto) {
        final var credentials = new UsernamePasswordAuthenticationToken(
                userCredentialsDto.username(),
                userCredentialsDto.password()
        );
        final var authenticatedUser = (UserDetailsImpl) authenticationManager
                .authenticate(credentials).getPrincipal();
        final String jwtToken = JwtUtils
                .buildJwtToken(
                        authenticatedUser.getUsername(),
                        jwtEncoder,
                        jwtProperties.getTokenDurationSeconds(),
                        JwtUtils.AuthorizationMethod.LOCAL
                );
        return new LoginOutputDataDto(jwtToken, authenticatedUser.getNickname());
    }
}
