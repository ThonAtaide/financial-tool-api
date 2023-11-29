package br.com.financialtoolapi.integration.api.v1;

import br.com.financialtoolapi.api.controller.v1.request.LoginRequestV1;
import br.com.financialtoolapi.api.controller.v1.response.ErrorResponseV1;
import br.com.financialtoolapi.api.controller.v1.response.LoginResponseV1;
import br.com.financialtoolapi.application.domain.entities.security.UserCredentialDataEntity;
import br.com.financialtoolapi.integration.api.AbstractApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static br.com.financialtoolapi.api.ErrorType.AUTHENTICATION_FAIL_BAD_CREDENTIALS;
import static br.com.financialtoolapi.api.ErrorType.PAYLOAD_DATA_VALIDATION_FAIL;
import static br.com.financialtoolapi.api.utils.CookieUtils.ACCESS_TOKEN_COOKIE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthenticationControllerSignInTest extends AbstractApiTest {

    private static final String SIGN_IN_REQUEST_URL = "/sign-in";

    @Test
    @DisplayName("Given that user has a valid an active account " +
            "When user try sign in with correct credentials " +
            "Then api should return https status ok, a payload with nickname " +
            "And a token on cookie")
    void testSuccessfullySignInWhenUserHasAValidAndActiveAccount() {
        final UserCredentialDataEntity userCredentialData = createUserAccountOnDatabase();
        final String nickname = userCredentialData.getUserAccount().getUserProfileData().getNickname();
        final LoginRequestV1 loginRequestV1 = new LoginRequestV1(
                userCredentialData.getUsername(),
                userCredentialData.getPassword()
        );

        ResponseEntity<LoginResponseV1> response = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_IN_REQUEST_URL),
                        loginRequestV1,
                        LoginResponseV1.class
                );
        final List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nickname()).isEqualTo(nickname);
        assertThat(cookies).isNotNull();
        assertThat(cookies.isEmpty()).isFalse();
        assertThat(cookies.get(0)).startsWith(ACCESS_TOKEN_COOKIE);
    }

    @Test
    @DisplayName("Given that user has a valid an active account " +
            "When user try sign in with wrong username " +
            "Then api should return https status UNAUTHORIZED, " +
            "and a error response describing bad credentials error."
            )
    void testBadCredentialsSignInWhenUsernameIsWrong() {
        final UserCredentialDataEntity userCredentialData = createUserAccountOnDatabase();
        final LoginRequestV1 loginRequestV1 = new LoginRequestV1(
                UUID.randomUUID().toString(),
                userCredentialData.getPassword()
        );

        ResponseEntity<ErrorResponseV1> response = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_IN_REQUEST_URL),
                        loginRequestV1,
                        ErrorResponseV1.class
                );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(AUTHENTICATION_FAIL_BAD_CREDENTIALS.getHttpStatus());
        assertThat(response.getBody().title()).isEqualTo(AUTHENTICATION_FAIL_BAD_CREDENTIALS.getTitle());
        assertThat(response.getBody().errorType()).isEqualTo(AUTHENTICATION_FAIL_BAD_CREDENTIALS);
        assertThat(response.getBody().statusCode()).isEqualTo(AUTHENTICATION_FAIL_BAD_CREDENTIALS.getHttpStatus().value());
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().instance()).isEqualTo(SIGN_IN_REQUEST_URL);
        assertThat(response.getBody().errors().size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Given that user has a valid an active account " +
            "When user try sign in without inform the username " +
            "Then api should return https status BAD REQUEST, " +
            "and a error response describing the missing data from payload."
    )
    void testArgumentNotValidSignInWhenUsernameIsNull() {
        createUserAccountOnDatabase();
        final LoginRequestV1 loginRequestV1 = new LoginRequestV1(
                null,
                null
        );

        ResponseEntity<ErrorResponseV1> response = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_IN_REQUEST_URL),
                        loginRequestV1,
                        ErrorResponseV1.class
                );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus());
        assertThat(response.getBody().title()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL.getTitle());
        assertThat(response.getBody().errorType()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL);
        assertThat(response.getBody().statusCode()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus().value());
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().instance()).isEqualTo(SIGN_IN_REQUEST_URL);
        assertThat(response.getBody().errors().size()).isEqualTo(2);
    }
}
