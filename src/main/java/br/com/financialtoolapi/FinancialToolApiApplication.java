package br.com.financialtoolapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class FinancialToolApiApplication {

    public static void main(String[] args) {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        System.out.println(UUID.randomUUID());
        System.out.println(bCrypt.encode("123456"));
        SpringApplication.run(FinancialToolApiApplication.class, args);
    }

}
