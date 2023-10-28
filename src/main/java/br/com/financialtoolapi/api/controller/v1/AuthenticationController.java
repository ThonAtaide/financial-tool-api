package br.com.financialtoolapi.api.controller.v1;

import br.com.financialtoolapi.api.controller.v1.request.LoginRequestV1;
import br.com.financialtoolapi.api.controller.v1.response.LoginResponseV1;
import br.com.financialtoolapi.api.utils.CookieUtils;
import br.com.financialtoolapi.infrastructure.config.properties.CookieProperties;
import br.com.financialtoolapi.infrastructure.security.services.LocalAuthenticationService;
import br.com.financialtoolapi.infrastructure.security.dto.UserCredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static br.com.financialtoolapi.infrastructure.config.security.filters.HeaderAppenderFilter.X_USER_IDENTIFIER_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final CookieProperties cookieProperties;
    private final LocalAuthenticationService localAuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestV1 loginRequestV1) {
        try {
            final var loginOutputDataDto = localAuthenticationService
                    .login(new UserCredentialsDto(loginRequestV1.username(), loginRequestV1.password()));
            final ResponseCookie cookie = CookieUtils
                    .buildCookieWith(loginOutputDataDto.token(), cookieProperties.getTokenCookieDurationSeconds());
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new LoginResponseV1(loginOutputDataDto.nickname()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
            final ResponseCookie cookie = CookieUtils
                    .buildCookieWith(null, cookieProperties.getTokenCookieDurationSeconds());
            return ResponseEntity
                    .noContent()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .build();
    }

    @PostMapping("/teste")
    public ResponseEntity<?> teste(@RequestHeader(name = X_USER_IDENTIFIER_HEADER) String userIdentifier) {

        return ResponseEntity.ok().build();
    }


}
