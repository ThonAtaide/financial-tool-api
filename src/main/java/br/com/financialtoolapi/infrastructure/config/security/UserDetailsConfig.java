package br.com.financialtoolapi.infrastructure.config.security;

import br.com.financialtoolapi.application.dtos.out.UserAuthenticationOutputDto;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.ports.in.security.LocalUserDetailsServicePort;
import br.com.financialtoolapi.infrastructure.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConfig {

    private final LocalUserDetailsServicePort localUserDetailsServicePort;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            try {
                final UserAuthenticationOutputDto userCredentials = localUserDetailsServicePort
                        .fetchUserCredentialsByUsername(username);
                return new UserDetailsImpl(
                        userCredentials.username(),
                        userCredentials.password(),
                        userCredentials.nickname(),
                        userCredentials.email()
                );
            } catch (ResourceNotFoundException ex) {
                throw new UsernameNotFoundException(ex.getMessage());
            }
        };
    }
}
