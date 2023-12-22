package br.com.financialtoolapi.integration.api;

import br.com.financialtoolapi.api.controller.v1.request.LoginRequestV1;
import br.com.financialtoolapi.api.controller.v1.response.LoginResponseV1;
import br.com.financialtoolapi.application.domain.entities.UserCredentialDataEntity;
import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.factory.user.UserFactory;
import br.com.financialtoolapi.integration.AbstractIntegrationTest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.List;
import java.util.UUID;

public abstract class AbstractApiTest extends AbstractIntegrationTest {

    private static final String SERVER_URL = "http://localhost:";
    private static final String SIGN_IN_REQUEST_URL = "/sign-in";

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private UserCredentialDataEntityRepository userCredentialDataEntityRepository;

    @BeforeEach
    void init() {
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(List.of()).build();
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    private String getServerUrlWithPortConcatenated() {
        return SERVER_URL.concat(String.valueOf(port));
    }

    protected String concatServerUrlWithResourcePath(final String resourcePath) {
        return getServerUrlWithPortConcatenated().concat(resourcePath);
    }

    protected UserCredentialDataEntity createUserAccountOnDatabase() {
        final String password = UUID.randomUUID().toString();
        final UserCredentialDataEntity userCredentialData = UserFactory
                .buildUserCredentialsEntity(passwordEncoder.encode(password));
        final UserCredentialDataEntity createdUser = userCredentialDataEntityRepository
                .saveAndFlush(userCredentialData);
        createdUser.setPassword(password);
        return createdUser;
    }

    protected HttpHeaders extractAccessTokenCookieFromHeader(
            final ResponseEntity<LoginResponseV1> loginResponseV1
    ) {
        return loginResponseV1
                .getHeaders();
    }

    protected HttpHeaders signInRandomUserAndExtractAccessTokenHeaders() {
        final UserCredentialDataEntity userCredentialData = createUserAccountOnDatabase();
        final LoginRequestV1 loginRequestV1 = new LoginRequestV1(
                userCredentialData.getUsername(),
                userCredentialData.getPassword()
        );

        final ResponseEntity<LoginResponseV1> signInResponse = restTemplate
                .postForEntity(
                        concatServerUrlWithResourcePath(SIGN_IN_REQUEST_URL),
                        loginRequestV1,
                        LoginResponseV1.class
                );
        return extractAccessTokenCookieFromHeader(signInResponse);
    }
}
