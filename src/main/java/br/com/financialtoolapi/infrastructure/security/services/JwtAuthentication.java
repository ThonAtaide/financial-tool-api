package br.com.financialtoolapi.infrastructure.security.services;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private JwtTokenAuthenticationPrincipal principal;

    public JwtAuthentication(final String userEmail) {
        super(null);
        this.principal = new JwtTokenAuthenticationPrincipal(userEmail);
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        principal = null;
    }

    public record JwtTokenAuthenticationPrincipal(String email) {
    }
}
