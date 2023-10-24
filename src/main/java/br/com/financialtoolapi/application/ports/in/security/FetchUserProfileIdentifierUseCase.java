package br.com.financialtoolapi.application.ports.in.security;

import java.util.UUID;

public interface FetchUserProfileIdentifierUseCase {

    UUID fetchUserProfileIdentifierByUsername(String username);
}
