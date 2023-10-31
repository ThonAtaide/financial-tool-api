package br.com.financialtoolapi.infrastructure.config.security;

import br.com.financialtoolapi.application.ports.in.security.UserAccountPort;
import br.com.financialtoolapi.infrastructure.config.security.filters.HeaderAppenderFilter;
import br.com.financialtoolapi.infrastructure.config.security.resolvers.BearerTokenCookieResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final BearerTokenCookieResolver bearerTokenCookieResolver;
    private final UserAccountPort userAccountPort;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizer ->
                        authorizer
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/auth/login").permitAll()
                                .anyRequest().authenticated()
                ).oauth2ResourceServer(oauth2Configurer ->
                        oauth2Configurer.jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .bearerTokenResolver(bearerTokenCookieResolver)
                ).addFilterAfter(new HeaderAppenderFilter(userAccountPort), BearerTokenAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCrypt() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
