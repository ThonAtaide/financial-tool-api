package br.com.financialtoolapi.infrastructure.config.security.filters;

import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.ports.in.business.UserAccountManagementPort;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import br.com.financialtoolapi.controller.errorhandler.ErrorType;
import br.com.financialtoolapi.infrastructure.security.services.JwtAuthentication;
import br.com.financialtoolapi.infrastructure.security.services.JwtAuthentication.JwtTokenAuthenticationPrincipal;
import br.com.financialtoolapi.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class HeaderAppenderFilter extends OncePerRequestFilter {

    public static final String X_USER_IDENTIFIER_HEADER = "x_user_identifier_header";
    private final UserAccountManagementPort userAccountPort;
    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse httpServletResponse,
            FilterChain chain
    ) throws ServletException, IOException {

        if (isUserAuthenticated()) {
            try {
                final CustomRequestWrapper customRequestWrapper = appendAccountIdIntoHeader(request);
                chain.doFilter(customRequestWrapper, httpServletResponse);
            } catch (ResourceNotFoundException ex) {
                final String errorMessage = InternationalizationUtils.getMessage(messageSource, ErrorType.AUTHENTICATION_TOKEN_MISSING.getTitleMessageCode());
                ResponseCookie tokenCookieClean = CookieUtils.buildCookieWith("", 0L);

                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.getWriter().write(errorMessage);
                httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, tokenCookieClean.toString());
                httpServletResponse.getWriter().flush();
            } catch (Exception ex) {
                final String errorMessage = InternationalizationUtils.getMessage(messageSource, ErrorType.UNEXPECTED_INTERNAL_ERROR.getTitleMessageCode());
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                httpServletResponse.getWriter().write(errorMessage);
                httpServletResponse.getWriter().flush();
            }

        } else {
            chain.doFilter(request, httpServletResponse);
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

    private JwtAuthentication getJwtAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication) {
            return (JwtAuthentication) authentication;
        }
        throw new RuntimeException("Unknown authentication method " + authentication.getClass());
    }

    private UUID getUserProfileIdentifier() {
        final JwtTokenAuthenticationPrincipal principal = (JwtTokenAuthenticationPrincipal) getJwtAuthentication().getPrincipal();
        return userAccountPort.fetchUserAccountIdentifierByEmail(principal.email());
    }
}
