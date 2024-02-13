package br.com.financialtoolapi.integration.controller.v1;

import br.com.financialtoolapi.controller.v1.request.LoginRequestV1;
import br.com.financialtoolapi.controller.errorhandler.ErrorResponse;
import br.com.financialtoolapi.controller.v1.response.LoginResponseV1;
import br.com.financialtoolapi.application.domain.entities.UserCredentialDataEntity;
import br.com.financialtoolapi.integration.controller.AbstractApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static br.com.financialtoolapi.controller.errorhandler.ErrorType.AUTHENTICATION_FAIL_BAD_CREDENTIALS;
import static br.com.financialtoolapi.controller.errorhandler.ErrorType.PROVIDED_DATA_VALIDATION_FAIL;
import static br.com.financialtoolapi.controller.errorhandler.CustomExceptionHandler.ARGUMENT_NOT_VALID_EXCEPTION_DEVELOPER_MESSAGE;
//import static br.com.financialtoolapi.utils.CookieUtils.ACCESS_TOKEN_COOKIE;
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
        final String nickname = userCredentialData.getUserAccount().getNickname();
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
        assertThat(cookies.get(0)).startsWith(HttpHeaders.AUTHORIZATION);
    }

    @Test
    @DisplayName("Given that user has a valid an active account " +
            "When user try sign in with wrong username " +
            "Then api should return https status UNAUTHORIZED, " +
            "and a error response describing bad credentials error."
            )
    void testBadCredentialsSignInWhenUsernameIsWrong() {
        final UserCredentialDataEntity userCredentialData = createUserAccountOnDatabase();
        final String expectedErrorTitle = "Authentication failed.";
        final String expectedErrorMessage = "Invalid username or password.";
        final LoginRequestV1 loginRequestV1 = new LoginRequestV1(
                UUID.randomUUID().toString(),
                userCredentialData.getPassword()
        );

        final ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_IN_REQUEST_URL),
                        loginRequestV1,
                        ErrorResponse.class
                );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(AUTHENTICATION_FAIL_BAD_CREDENTIALS.getHttpStatus());
        assertThat(response.getBody().title()).isEqualTo(expectedErrorTitle);
        assertThat(response.getBody().errorType()).isEqualTo(AUTHENTICATION_FAIL_BAD_CREDENTIALS);
        assertThat(response.getBody().statusCode()).isEqualTo(AUTHENTICATION_FAIL_BAD_CREDENTIALS.getHttpStatus().value());
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().instance()).isEqualTo(SIGN_IN_REQUEST_URL);
        assertThat(response.getBody().errors().size()).isEqualTo(1);
        assertThat(response.getBody().errors().get(0)).isEqualTo(expectedErrorMessage);
        assertThat(response.getBody().developerInfo()).isNotNull();

    }

    @Test
    @DisplayName("Given that user has a valid an active account " +
            "When user try sign in without inform the username and password" +
            "Then api should return https status BAD REQUEST, " +
            "and a error response describing the missing data from payload."
    )
    void testArgumentNotValidSignInWhenUsernameAndPasswordIsNull() {
        createUserAccountOnDatabase();
        final String expectedErrorTitle = "Invalid or incomplete data.";
        final List<String> expectedMessages = List.of(
                "Username is required for signing.",
                "Password is required for signing."
        );
        final LoginRequestV1 loginRequestV1 = new LoginRequestV1(
                null,
                null
        );

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_IN_REQUEST_URL),
                        loginRequestV1,
                        ErrorResponse.class
                );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(PROVIDED_DATA_VALIDATION_FAIL.getHttpStatus());
        assertThat(response.getBody().title()).isEqualTo(expectedErrorTitle);
        assertThat(response.getBody().errorType()).isEqualTo(PROVIDED_DATA_VALIDATION_FAIL);
        assertThat(response.getBody().statusCode()).isEqualTo(PROVIDED_DATA_VALIDATION_FAIL.getHttpStatus().value());
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().instance()).isEqualTo(SIGN_IN_REQUEST_URL);
        assertThat(response.getBody().errors().containsAll(expectedMessages)).isTrue();
        assertThat(response.getBody().developerInfo()).isEqualTo(ARGUMENT_NOT_VALID_EXCEPTION_DEVELOPER_MESSAGE);
    }
}
