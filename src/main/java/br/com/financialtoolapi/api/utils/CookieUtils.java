package br.com.financialtoolapi.api.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseCookie;

@UtilityClass
public class CookieUtils {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";

    public ResponseCookie buildCookieWith(
            final String token,
            final Long cookieDurationSeconds
    ) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, token)
                .maxAge(cookieDurationSeconds)
                .httpOnly(true)
                .path("/")
                .build();
    }
}
