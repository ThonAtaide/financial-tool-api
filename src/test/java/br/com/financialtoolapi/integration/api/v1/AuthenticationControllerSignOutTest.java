package br.com.financialtoolapi.integration.api.v1;

import br.com.financialtoolapi.integration.api.AbstractApiTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthenticationControllerSignOutTest extends AbstractApiTest {

    private static final String SIGN_OUT_REQUEST_URL = "/sign-out";

    @Test
    @DisplayName("Given that user has signed on application " +
            "When user try to sign out " +
            "Then api should clean the access token from cookie and return Http status no content"
    )
    void testSuccessfullySignOutWhenUserHasAValidAccessToken() {
        final HttpHeaders headers = signInRandomUserAndExtractAccessTokenHeaders();
        final ResponseEntity<Object> signOutResponse = restTemplate
                .exchange(
                        concatServerUrlWithResourcePath(SIGN_OUT_REQUEST_URL),
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        Object.class
                );

        assertThat(signOutResponse.getBody()).isNull();
        assertThat(signOutResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Given that user has not signed on application " +
            "When user try sign out " +
            "Then api should return https status UNAUTHORIZED"
    )
    void testWhenUserHasNotSignInAndTriesToSignOut() {

        final ResponseEntity<Object> signOutResponse = restTemplate
                .exchange(
                        concatServerUrlWithResourcePath(SIGN_OUT_REQUEST_URL),
                        HttpMethod.POST,
                        new HttpEntity<>(null),
                        Object.class
                );

        assertThat(signOutResponse.getBody()).isNotNull();
        assertThat(signOutResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @SneakyThrows
    @Test
    @DisplayName("Given that user access token has expired " +
            "When user try sign out " +
            "Then api should return https status UNAUTHORIZED"
    )
    void testWhenUserAccessTokenHasExpiredAndUserTriesToLogout() {
        final HttpHeaders headers = signInRandomUserAndExtractAccessTokenHeaders();
        Thread.sleep(2000L);
        final ResponseEntity<Object> signOutResponse = restTemplate
                .exchange(
                        concatServerUrlWithResourcePath(SIGN_OUT_REQUEST_URL),
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        Object.class
                );

        assertThat(signOutResponse.getBody()).isNotNull();
        assertThat(signOutResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

}
