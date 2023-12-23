package br.com.financialtoolapi.application.adapters.in.security;

import br.com.financialtoolapi.application.usecases.security.FindUserCredentialDataByUsernameUseCase;
import br.com.financialtoolapi.application.mapper.AuthenticationMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceAdapter implements UserDetailsService {

    private final AuthenticationMapper authenticationMapper = Mappers.getMapper(AuthenticationMapper.class);
    private final FindUserCredentialDataByUsernameUseCase findUserCredentialDataByUsernameUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserCredentialDataByUsernameUseCase
                .findUserCredentialsByUsername(username)
                .map(authenticationMapper::toUserDetailsDto)
                .orElseThrow(() -> new UsernameNotFoundException(
                                String.format("User from username %s was not found", username)
                        )
                );
    }
}
