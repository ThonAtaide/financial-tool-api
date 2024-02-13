package br.com.financialtoolapi.infrastructure.config.security.filters;

import br.com.financialtoolapi.infrastructure.security.services.JwtAuthentication;
import br.com.financialtoolapi.infrastructure.security.services.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = resolveToken(httpServletRequest);
        try {
            if (token != null) {
                final Jws<Claims> parsedToken = jwtTokenService.validateTokenAndReturnParsedToken(token);
                final JwtAuthentication authentication = new JwtAuthentication(parsedToken.getPayload().getSubject());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private String resolveToken(HttpServletRequest request) {
        return Optional.ofNullable(request)
                .map(HttpServletRequest::getCookies)
                .filter(this::cookiesIsNotEmpty)
                .map(this::getCookieAccessTokenIfExists
                ).orElse(null);
    }

    private String getCookieAccessTokenIfExists(final @NonNull Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> HttpHeaders.AUTHORIZATION.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(this::tokenStringIsNotEmpty)
                .findAny()
                .orElse(null);
    }

    private boolean cookiesIsNotEmpty(final Cookie[] cookies) {
        return cookies.length > 0;
    }

    private boolean tokenStringIsNotEmpty(final String token) {
        return token == null || !token.isEmpty();
    }
}
