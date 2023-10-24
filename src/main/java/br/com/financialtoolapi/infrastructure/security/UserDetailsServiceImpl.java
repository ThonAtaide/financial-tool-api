package br.com.financialtoolapi.infrastructure.security;

import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.model.dto.out.UserLoginOutputDto;
import br.com.financialtoolapi.application.ports.in.security.AuthenticationUseCase;
import br.com.financialtoolapi.infrastructure.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthenticationUseCase authenticationUseCase;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {
            final UserLoginOutputDto userCredentials = authenticationUseCase
                    .fetchUserCredentialsByUsername(username);
            return new UserDetailsImpl(userCredentials.username(), userCredentials.password(), userCredentials.nickname());
        } catch (ResourceNotFoundException ex) {
            throw new UsernameNotFoundException(ex.getMessage());
        }
    }
}
