package br.com.financialtoolapi.infrastructure.security.services;

import br.com.financialtoolapi.infrastructure.config.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public String buildToken(
            final String subject
    ) {
        final Instant now = Instant.now();

        final JwsHeader jwsHeader = JwsHeader.with(HS256).build();
        final JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuer("financial-tool")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtProperties.getTokenDurationSeconds()))
                .subject(subject)
                .build();
        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }
}
