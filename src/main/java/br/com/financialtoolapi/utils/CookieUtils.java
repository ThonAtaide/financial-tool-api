package br.com.financialtoolapi.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class CookieUtils {

    public ResponseCookie buildCookieWith(
            final String token,
            final Long cookieDurationMilliSeconds,
            @NonNull final String[] activesProfiles
    ) {
        ResponseCookieBuilder cookieBuilder = ResponseCookie.from(HttpHeaders.AUTHORIZATION, token)
                .maxAge(cookieDurationMilliSeconds / 1000)
                .httpOnly(true)
                .path("/");

        if (isTestProfileNotActive(activesProfiles)) {
            log.debug("Test profile is not active so cookie samesite is none and secure");
            cookieBuilder
                    .secure(true)
                    .sameSite(Cookie.SameSite.NONE.attributeValue());
        } else {
            log.debug("Test profile is active");
        }

        return cookieBuilder.build();
    }

    private boolean isTestProfileNotActive(@NonNull final String[] activesProfiles) {
        return !Arrays.asList(activesProfiles).contains("Test");
    }
}
