package br.com.financialtoolapi.integration.controller.v1;

import br.com.financialtoolapi.controller.v1.request.UserRegisterRequestV1;
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

import static br.com.financialtoolapi.controller.errorhandler.ErrorType.PAYLOAD_DATA_VALIDATION_FAIL;
import static br.com.financialtoolapi.controller.errorhandler.CustomExceptionHandler.ARGUMENT_NOT_VALID_EXCEPTION_DEVELOPER_MESSAGE;
import static br.com.financialtoolapi.utils.CookieUtils.ACCESS_TOKEN_COOKIE;
import static br.com.financialtoolapi.application.validations.userinfo.ValidateIfUsernameIsAvailable.DETAILED_ERROR_MESSAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthenticationControllerSignUpTest extends AbstractApiTest {

    private static final String SIGN_UP_REQUEST_URL = "/sign-up";

    @Test
    @DisplayName("Given a valid user sign up payload " +
            "When user try submitted it " +
            "Then api should return https status created, a payload with nickname " +
            "And a token on cookie")
    void testSuccessfullySignUpWhenUserInsertAValidPayload() {
        final String nickname = UUID.randomUUID().toString().substring(0, 9);
        final var userRegisterRequest = new UserRegisterRequestV1(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                nickname
        );

        ResponseEntity<LoginResponseV1> response = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_UP_REQUEST_URL),
                        userRegisterRequest,
                        LoginResponseV1.class
                );
        final List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nickname()).isEqualTo(nickname);
        assertThat(cookies).isNotNull();
        assertThat(cookies.isEmpty()).isFalse();
        assertThat(cookies.get(0)).startsWith(ACCESS_TOKEN_COOKIE);
    }

    @Test
    @DisplayName("Given a empty sign up payload " +
            "When user try sign up without the fields filled " +
            "Then api should return https status BAD REQUEST, " +
            "and a error response describing the missing data from payload."
    )
    void testArgumentNotValidSignInWhenFieldsAreNull() {
        final var expectedErrorTitle = "Invalid or incomplete data.";
        final UserRegisterRequestV1 userRegisterRequest =
                new UserRegisterRequestV1(null, null, null, null);

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_UP_REQUEST_URL),
                        userRegisterRequest,
                        ErrorResponse.class
                );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus());
        assertThat(response.getBody().title()).isEqualTo(expectedErrorTitle);
        assertThat(response.getBody().developerInfo()).isEqualTo(ARGUMENT_NOT_VALID_EXCEPTION_DEVELOPER_MESSAGE);
        assertThat(response.getBody().errors().size()).isEqualTo(4);
        assertThat(response.getBody().errorType()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL);
        assertThat(response.getBody().statusCode()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus().value());
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().instance()).isEqualTo(SIGN_UP_REQUEST_URL);
    }

    @Test
    @DisplayName("Given that user sign up payload has a username already in use " +
            "When user submit the request " +
            "Then api should return https status BAD REQUEST, " +
            "and a error response describing validation data error."
    )
    void testWhenUserSignUpPayloadHasAUsernameAlreadyUsedByOtherUser() {
        final String expectedErrorTitle = "Invalid or incomplete data.";
        final List<String> expectedErrorMessages = List.of("The username is already taken.");
        final UserCredentialDataEntity userCredentialData = createUserAccountOnDatabase();
        final UserRegisterRequestV1 userRegisterRequest = new UserRegisterRequestV1(
                userCredentialData.getUsername(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString().substring(0, 9)
        );

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_UP_REQUEST_URL),
                        userRegisterRequest,
                        ErrorResponse.class
                );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus());
        assertThat(response.getBody().title()).isEqualTo(expectedErrorTitle);
        assertThat(response.getBody().errorType()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL);
        assertThat(response.getBody().statusCode()).isEqualTo(PAYLOAD_DATA_VALIDATION_FAIL.getHttpStatus().value());
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().instance()).isEqualTo(SIGN_UP_REQUEST_URL);
        assertThat(response.getBody().errors().containsAll(expectedErrorMessages)).isTrue();
        assertThat(response.getBody().developerInfo()).isEqualTo(String.format(DETAILED_ERROR_MESSAGE, userRegisterRequest.username()));

    }
}
