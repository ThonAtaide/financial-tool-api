package br.com.financialtoolapi.infrastructure.config.security;

import br.com.financialtoolapi.controller.errorhandler.ErrorResponse;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static br.com.financialtoolapi.controller.errorhandler.ErrorType.AUTHENTICATION_TOKEN_MISSING;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String  MISSING_ACCESS_TOKEN_MESSAGE_CODE= "sign-in.access-token-missing.error-message";

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException
    ) throws IOException {

        final String instance = request.getServletPath();
        final String messageTitle = InternationalizationUtils
                .getMessage(messageSource, AUTHENTICATION_TOKEN_MISSING.getTitleMessageCode());
        final ErrorResponse errorResponse = new ErrorResponse(
                messageTitle,
                List.of(InternationalizationUtils.getMessage(messageSource, MISSING_ACCESS_TOKEN_MESSAGE_CODE)),
                AUTHENTICATION_TOKEN_MISSING,
                HttpStatus.UNAUTHORIZED.value(),
                instance,
                Instant.now(),
                "Authentication failed due to missing or expired token."
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final var responseStream = response.getOutputStream();
        objectMapper.writeValue(responseStream, errorResponse);
        responseStream.flush();
    }
}
