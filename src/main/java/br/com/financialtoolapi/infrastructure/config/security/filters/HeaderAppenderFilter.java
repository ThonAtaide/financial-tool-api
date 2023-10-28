package br.com.financialtoolapi.infrastructure.config.security.filters;

import br.com.financialtoolapi.application.ports.in.security.UserAccountPort;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class HeaderAppenderFilter implements Filter {

    public static final String JWT_SUBJECT = "sub";
    public static final String X_USER_IDENTIFIER_HEADER = "x_user_identifier_header";
    private final UserAccountPort userAccountPort;

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {

        if (isUserAuthenticated()) {
            final UUID userProfileIdentifier = getUserProfileIdentifier();
            final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            final CustomRequestWrapper customRequestWrapper = new CustomRequestWrapper(httpServletRequest);
            customRequestWrapper.putHeader(X_USER_IDENTIFIER_HEADER, userProfileIdentifier.toString());
            chain.doFilter(customRequestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private JwtAuthenticationToken getJwtAuthenticationToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            return (JwtAuthenticationToken) authentication;
        }
        throw new RuntimeException("Unknown authentication method " + authentication.getClass());
    }

    private UUID getUserProfileIdentifier() {
        final Map<String, Object> tokenAttributes = getJwtAuthenticationToken().getTokenAttributes();
        final String jwtSubject = (String) tokenAttributes.get(JWT_SUBJECT);
        return userAccountPort.fetchUserAccountIdentifierByEmail(jwtSubject);
    }
}
