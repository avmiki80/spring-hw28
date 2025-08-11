package ru.otus.spring.hw26.book.feign;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.hw26.book.service.CustomSecurityContextService;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class OAuth2FeignConfig {
    private final CustomSecurityContextService customSecurityContextService;
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            String jwtToken = customSecurityContextService.getJwtToken();
            if (Objects.nonNull(jwtToken))
                requestTemplate.header("Authorization", "Bearer " + jwtToken);
        };
    }
}
