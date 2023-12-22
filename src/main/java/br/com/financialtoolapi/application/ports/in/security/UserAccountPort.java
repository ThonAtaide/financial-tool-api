package br.com.financialtoolapi.application.ports.in.security;

import java.util.UUID;

public interface UserAccountPort {

    UUID fetchUserAccountIdentifierByEmail(String email);
}
