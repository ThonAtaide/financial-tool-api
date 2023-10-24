package br.com.financialtoolapi.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "financial-tool.cookie-properties")
public class CookieProperties {
    private Long tokenCookieDurationSeconds = 360L;
}
