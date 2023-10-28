package br.com.financialtoolapi.infrastructure.config.security.resolvers;

import br.com.financialtoolapi.api.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.Arrays;
import java.util.Optional;

@Configuration
public class BearerTokenCookieResolver implements BearerTokenResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        return Optional.ofNullable(request)
                .map(HttpServletRequest::getCookies)
                .filter(this::cookiesIsNotEmpty)
                .map(this::getCookieAccessTokenIfExists
                ).orElse(null);
    }

    private String getCookieAccessTokenIfExists(final @NonNull Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> CookieUtils.ACCESS_TOKEN_COOKIE.equals(cookie.getName()))
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
