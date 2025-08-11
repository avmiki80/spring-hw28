package ru.otus.spring.hw26.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class CustomSecurityConfig {
    private final String jwkUri;
    @Autowired
    public CustomSecurityConfig(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkUri) {
        this.jwkUri = jwkUri;
        log.info("jwkUri: {}", jwkUri);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
//                                "/api/loan/actuator/**",
                                "/api/loan/swagger-ui.html",
                                "/api/loan/swagger-ui/**",
                                "/api/loan/v3/api-docs/**",
                                "/api/swagger-ui.html",
                                "/api/swagger-ui/**",
                                "/api/v3/api-docs/**"
//                                "/api/actuator/**"
                        ).permitAll()
//                        .pathMatchers("/api/book/**", "/api/genre/**", "/api/author/**", "/api/comment/**").authenticated()
                        .pathMatchers("/api/**").authenticated()
                        .anyExchange().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwkSetUri(jwkUri))
                )
                .csrf().disable();

        return http.build();
    }

}
