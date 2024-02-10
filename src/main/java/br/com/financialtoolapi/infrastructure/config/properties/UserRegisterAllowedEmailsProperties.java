package br.com.financialtoolapi.infrastructure.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.Delimiter;

import java.util.List;

@Getter
@ConfigurationProperties("financial-tool.white-lists.user-allowed-emails")
public class UserRegisterAllowedEmailsProperties {

    private final boolean enable;

    private final List<String> emailsList;

    public UserRegisterAllowedEmailsProperties(
            final boolean enable,
            final @Delimiter(",") List<String> emailsList
    ) {
        this.enable = enable;
        this.emailsList = emailsList;
    }
}
