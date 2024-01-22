package br.com.financialtoolapi.infrastructure.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("financial-tool.white-lists.user-allowed-emails")
public class UserRegisterAllowedEmailsProperties {

    private final boolean enable;
    private final List<String> emailsList;
}
