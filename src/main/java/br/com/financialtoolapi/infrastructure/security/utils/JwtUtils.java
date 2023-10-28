package br.com.financialtoolapi.infrastructure.security.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

import static org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256;

@UtilityClass
public class JwtUtils {

    public String buildJwtToken(
            final String subject,
            final JwtEncoder jwtEncoder,
            final Long tokenDuration
    ) {
        final Instant now = Instant.now();

        final JwsHeader jwsHeader = JwsHeader.with(HS256).build();
        final JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuer("financial-tool")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenDuration))
                .subject(subject)
                .build();
        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }
}
