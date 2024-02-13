package br.com.financialtoolapi.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

@UtilityClass
public class CookieUtils {

    public ResponseCookie buildCookieWith(
            final String token,
            final Long cookieDurationMilliSeconds
    ) {
        return ResponseCookie.from(HttpHeaders.AUTHORIZATION, token)
                .maxAge(cookieDurationMilliSeconds / 1000)
                .httpOnly(true)
                .path("/")
                .build();
    }
}
