package br.com.financialtoolapi.infrastructure.config.security.filters;

import br.com.financialtoolapi.application.ports.in.security.FetchUserProfileIdentifierUseCase;
import br.com.financialtoolapi.infrastructure.security.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static br.com.financialtoolapi.infrastructure.security.utils.JwtUtils.AUTHORIZATION_METHOD;

@RequiredArgsConstructor
public class HeaderAppenderFilter implements Filter {

    public static final String JWT_SUBJECT = "sub";
    public static final String X_USER_IDENTIFIER_HEADER = "x_user_identifier_header";
    private final FetchUserProfileIdentifierUseCase fetchUserProfileIdentifierUseCase;

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
        final String authorizationMethod = (String) tokenAttributes.get(AUTHORIZATION_METHOD);
        final String jwtSubject = (String) tokenAttributes.get(JWT_SUBJECT);

        if (JwtUtils.AuthorizationMethod.LOCAL.name().equals(authorizationMethod)) {
            return fetchUserProfileIdentifierUseCase.fetchUserProfileIdentifierByUsername(jwtSubject);
        }
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
