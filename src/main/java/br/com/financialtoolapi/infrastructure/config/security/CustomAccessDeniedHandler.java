package br.com.financialtoolapi.infrastructure.config.security;

import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import br.com.financialtoolapi.controller.errorhandler.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static br.com.financialtoolapi.controller.errorhandler.ErrorType.ACCESS_FORBIDDEN;
import static br.com.financialtoolapi.controller.errorhandler.ErrorType.AUTHENTICATION_TOKEN_MISSING;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String  RESOURCE_ACCESS_FORBIDDEN = "resource.access-forbidden.error-message";

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    public void handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        log.info(accessDeniedException.getMessage());
        final String instance = request.getServletPath();
        final String messageTitle = InternationalizationUtils
                .getMessage(messageSource, ACCESS_FORBIDDEN.getTitleMessageCode());
        final ErrorResponse errorResponse = new ErrorResponse(
                messageTitle,
                List.of(InternationalizationUtils.getMessage(messageSource, RESOURCE_ACCESS_FORBIDDEN)),
                ACCESS_FORBIDDEN,
                ACCESS_FORBIDDEN.getHttpStatus().value(),
                instance,
                Instant.now(),
                "Resource access denied for user."
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        final var responseStream = response.getOutputStream();
        objectMapper.writeValue(responseStream, errorResponse);
        responseStream.flush();
    }
}
