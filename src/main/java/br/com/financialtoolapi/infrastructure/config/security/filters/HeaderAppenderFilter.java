package br.com.financialtoolapi.infrastructure.config.security.filters;

import br.com.financialtoolapi.controller.errorhandler.ErrorType;
import br.com.financialtoolapi.utils.CookieUtils;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.ports.in.business.UserAccountManagementPort;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final UserAccountManagementPort userAccountPort;
    private final MessageSource messageSource;

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {

        if (isUserAuthenticated()) {
            try {
                final CustomRequestWrapper customRequestWrapper = appendAccountIdIntoHeader(request);
                chain.doFilter(customRequestWrapper, response);
            } catch (ResourceNotFoundException ex) {
                final String errorMessage = InternationalizationUtils.getMessage(messageSource, ErrorType.AUTHENTICATION_TOKEN_MISSING.getTitleMessageCode());
                ResponseCookie tokenCookieClean = CookieUtils.buildCookieWith("", 0L);
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.getWriter().write(errorMessage);
                httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, tokenCookieClean.toString());
                httpServletResponse.getWriter().flush();
            } catch (Exception ex) {
                final String errorMessage = InternationalizationUtils.getMessage(messageSource, ErrorType.UNEXPECTED_INTERNAL_ERROR.getTitleMessageCode());
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                httpServletResponse.getWriter().write(errorMessage);
                httpServletResponse.getWriter().flush();
            }

        } else {
            chain.doFilter(request, response);
        }
    }

    private CustomRequestWrapper appendAccountIdIntoHeader(final ServletRequest request) {
        final UUID userProfileIdentifier = getUserProfileIdentifier();
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final CustomRequestWrapper customRequestWrapper = new CustomRequestWrapper(httpServletRequest);
        customRequestWrapper.putHeader(X_USER_IDENTIFIER_HEADER, userProfileIdentifier.toString());
        return customRequestWrapper;
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
