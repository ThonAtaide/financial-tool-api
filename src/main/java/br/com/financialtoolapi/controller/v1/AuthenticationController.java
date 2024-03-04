package br.com.financialtoolapi.controller.v1;

import br.com.financialtoolapi.application.dtos.out.LoggedUserDataDto;
import br.com.financialtoolapi.application.ports.in.business.UserAccountManagementPort;
import br.com.financialtoolapi.application.ports.in.security.LocalAuthenticationPort;
import br.com.financialtoolapi.controller.v1.mapper.UserDataMapper;
import br.com.financialtoolapi.controller.v1.request.LoginRequestV1;
import br.com.financialtoolapi.controller.v1.request.UserRegisterRequestV1;
import br.com.financialtoolapi.controller.v1.response.LoginResponseV1;
import br.com.financialtoolapi.infrastructure.config.properties.JwtProperties;
import br.com.financialtoolapi.infrastructure.security.services.JwtTokenService;
import br.com.financialtoolapi.utils.CookieUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "/",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AuthenticationController {

    private final JwtTokenService jwtTokenService;
    private final JwtProperties jwtProperties;
    private final LocalAuthenticationPort localAuthenticationPort;
    private final UserAccountManagementPort userAccountManagementPort;
    private final UserDataMapper userDataMapper = Mappers.getMapper(UserDataMapper.class);
    private final Environment environment;

    @PostMapping("sign-in")
    public ResponseEntity<LoginResponseV1> login(@Valid @RequestBody final LoginRequestV1 loginRequestV1) {
        final var authenticatedUser = localAuthenticationPort
                .login(userDataMapper.from(loginRequestV1));
        return this.generateJwtTokenAndResponseEntityWithCookie(authenticatedUser, HttpStatus.OK);
    }

    @PostMapping("sign-out")
    public ResponseEntity<Void> logout() {
        final ResponseCookie cookie = CookieUtils
                .buildCookieWith("null", 0L, environment.getActiveProfiles());
        return ResponseEntity
                .noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("sign-up")
    public ResponseEntity<LoginResponseV1> registerNewUser(@Valid @RequestBody final UserRegisterRequestV1 userRegisterRequest) {
        final var createdUserAuthenticated = userAccountManagementPort
                .createUser(this.userDataMapper.from(userRegisterRequest));
        return this.generateJwtTokenAndResponseEntityWithCookie(createdUserAuthenticated, HttpStatus.CREATED);
    }

    private ResponseEntity<LoginResponseV1> generateJwtTokenAndResponseEntityWithCookie(
            final LoggedUserDataDto loggedUserData,
            final HttpStatus httpStatus
    ) {
        final String jwtToken = jwtTokenService.buildToken(loggedUserData.email());
        final ResponseCookie cookie = CookieUtils
                .buildCookieWith(jwtToken, jwtProperties.getTokenDurationMilliseconds(), environment.getActiveProfiles());
        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponseV1(loggedUserData.nickname()));
    }
}
