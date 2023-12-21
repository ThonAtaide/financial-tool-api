package br.com.financialtoolapi;

import br.com.financialtoolapi.infrastructure.config.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {JwtProperties.class})
public class FinancialToolApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialToolApiApplication.class, args);
    }

}
