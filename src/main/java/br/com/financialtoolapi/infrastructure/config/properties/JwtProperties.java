package br.com.financialtoolapi.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "financial-tool.jwt-properties")
public class JwtProperties {
    private Long tokenDurationSeconds = 360L;
}
