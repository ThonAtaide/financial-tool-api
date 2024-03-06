package br.com.financialtoolapi.infrastructure.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "financial-tool.jwt-properties")
public class JwtProperties {

    private final Long tokenDurationMilliseconds;
    private final String secretKey;
}
