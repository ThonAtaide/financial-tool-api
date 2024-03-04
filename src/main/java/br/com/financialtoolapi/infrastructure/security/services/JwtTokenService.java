package br.com.financialtoolapi.infrastructure.security.services;

import br.com.financialtoolapi.infrastructure.config.properties.JwtProperties;
import br.com.financialtoolapi.infrastructure.security.services.exceptions.JwtTokenInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("${spring.application.name}")
    private String applicationName;

    private final JwtProperties jwtProperties;

    public String buildToken(
            final String subject
    ) {
        final SecretKeySpec secretKey = getSecretKeySpec();
        final Date issuedAt = new Date();
        final Date expiresAt = getExpirationDate(issuedAt);
        return Jwts.builder()
                .header()
                .type("jwt")
                .and()
                .issuer(applicationName)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .subject(subject)
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> validateTokenAndReturnParsedToken(final String jwtToken) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKeySpec())
                    .build()
                    .parseSignedClaims(jwtToken);
        } catch (Exception ex) {
            log.error("Jwt token validation fail - {}", ex.getMessage());
            throw new JwtTokenInvalidException(ex.getMessage());
        }
    }

    private Date getExpirationDate(final Date issuedAt) {
        final Long tokenDurationMilliseconds = jwtProperties.getTokenDurationMilliseconds();
        return new Date(issuedAt.getTime() + tokenDurationMilliseconds);
    }

    private SecretKeySpec getSecretKeySpec() {
        final String secretKeyProperty = jwtProperties.getSecretKey();
        return new SecretKeySpec(secretKeyProperty.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }
}
