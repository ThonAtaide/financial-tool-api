package br.com.financialtoolapi.api.controller.v1;

import br.com.financialtoolapi.api.controller.v1.mapper.UserDataMapper;
import br.com.financialtoolapi.api.controller.v1.request.LoginRequestV1;
import br.com.financialtoolapi.api.controller.v1.request.UserRegisterRequestV1;
import br.com.financialtoolapi.api.controller.v1.response.LoginResponseV1;
import br.com.financialtoolapi.api.utils.CookieUtils;
import br.com.financialtoolapi.application.exceptions.ResourceCreationException;
import br.com.financialtoolapi.application.ports.in.security.LocalAuthenticationPort;
import br.com.financialtoolapi.infrastructure.config.properties.JwtProperties;
import br.com.financialtoolapi.infrastructure.security.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final LocalAuthenticationPort localAuthenticationPort;
    private final UserDataMapper userDataMapper = Mappers.getMapper(UserDataMapper.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestV1 loginRequestV1) {
        try {
            return this.userLogin(loginRequestV1);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        final ResponseCookie cookie = CookieUtils
                .buildCookieWith(null, jwtProperties.getTokenDurationSeconds());
        return ResponseEntity
                .noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserRegisterRequestV1 userRegisterRequest) {
        try {
            localAuthenticationPort
                    .registerNewUser(this.userDataMapper.from(userRegisterRequest));
            return this.userLogin(new LoginRequestV1(userRegisterRequest.username(), userRegisterRequest.password()));
        } catch (ResourceCreationException ex) {
            log.error(String.valueOf(ex));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    private ResponseEntity<LoginResponseV1> userLogin(LoginRequestV1 loginRequestV1) {
        final var authenticatedUser = localAuthenticationPort
                .login(userDataMapper.from(loginRequestV1));
        final String jwtToken = jwtService.buildToken(authenticatedUser.email());
        final ResponseCookie cookie = CookieUtils
                .buildCookieWith(jwtToken, jwtProperties.getTokenDurationSeconds());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponseV1(authenticatedUser.nickname()));
    }
}
