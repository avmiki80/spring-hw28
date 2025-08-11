package ru.otus.spring.hw26.book.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.annotation.PostConstruct;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Slf4j
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
    private final String clientId;
    private final String jwkUri;
    public CustomSecurityConfig(
            @Value("${clientId}") String clientId,
            @Value("${jwk.uri}") String jwkUri
    ) {
        this.clientId = clientId;
        this.jwkUri = jwkUri;
    }
    // Чтоб интерцептор feign client(feign client работает в другом потоке) мог получить доступ SecurityContext
    // Все пишут, что вкл по умолчанию доступ SecurityContext в разных потокоах, а вот и хер!
    @PostConstruct
    public void enableAuthCtxOnSpawnedThreads() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(
                                "/actuator/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        )
                        .permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2
                                .accessDeniedHandler(customAccessDeniedHandler())
                                .jwt(jwt -> jwt.jwkSetUri(jwkUri))
                );
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new KeycloakJwtGrantedAuthoritiesConverter(clientId)
        );
        return jwtAuthenticationConverter;
    }
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
