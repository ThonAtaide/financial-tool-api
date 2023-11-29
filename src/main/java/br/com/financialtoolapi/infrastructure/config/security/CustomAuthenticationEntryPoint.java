package br.com.financialtoolapi.infrastructure.config.security;

import br.com.financialtoolapi.api.utils.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException
    ) throws IOException {
        final Map<Object, String> responseBody = Map.of(HttpStatus.UNAUTHORIZED, "User authentication failed.");

        final var cookie = ResponseCookie.from(CookieUtils.ACCESS_TOKEN_COOKIE, null)
                .maxAge(0)
                .httpOnly(true)
                .path("/")
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        final var responseStream = response.getOutputStream();
        final var mapper = new ObjectMapper();
        mapper.writeValue(responseStream, responseBody);
        responseStream.flush();
    }
}
