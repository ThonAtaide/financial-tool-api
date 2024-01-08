package br.com.financialtoolapi.infrastructure.config.security;

import br.com.financialtoolapi.application.ports.in.business.UserAccountManagementPort;
import br.com.financialtoolapi.application.ports.out.security.AuthenticationFrameworkWrapper;
import br.com.financialtoolapi.infrastructure.config.security.filters.HeaderAppenderFilter;
import br.com.financialtoolapi.infrastructure.config.security.resolvers.BearerTokenCookieResolver;
import br.com.financialtoolapi.infrastructure.security.services.LocalAuthenticationServiceFramework;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity httpSecurity,
            final UserAccountManagementPort userAccountPort,
            final CustomAuthenticationEntryPoint authenticationEntryPoint,
            final BearerTokenCookieResolver bearerTokenCookieResolver,
            final MessageSource messageSource
    ) throws Exception {

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizer ->
                        authorizer
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/sign-in").permitAll()
                                .requestMatchers("/sign-up").permitAll()
                                .requestMatchers(HttpMethod.GET, "/expenseCategories", "/expenseCategories/*").authenticated()
                                .requestMatchers(HttpMethod.POST, "/expenseCategories").denyAll()
                                .requestMatchers(HttpMethod.PUT, "/expenseCategories/**").denyAll()
                                .anyRequest().authenticated()
                ).oauth2ResourceServer(oauth2Configurer ->
                        oauth2Configurer.jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .bearerTokenResolver(bearerTokenCookieResolver)
                ).addFilterAfter(new HeaderAppenderFilter(userAccountPort, messageSource), BearerTokenAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("http://localhost:3000"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
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
