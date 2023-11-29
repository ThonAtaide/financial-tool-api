package br.com.financialtoolapi.integration.api;

import br.com.financialtoolapi.application.domain.entities.security.UserCredentialDataEntity;
import br.com.financialtoolapi.application.domain.repositories.UserCredentialDataEntityRepository;
import br.com.financialtoolapi.factory.user.UserFactory;
import br.com.financialtoolapi.integration.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

public abstract class AbstractApiTest extends AbstractIntegrationTest {

    private static final String SERVER_URL = "http://localhost:";

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private UserCredentialDataEntityRepository userCredentialDataEntityRepository;

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
}
