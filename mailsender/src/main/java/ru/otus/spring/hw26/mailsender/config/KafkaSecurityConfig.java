package ru.otus.spring.hw26.mailsender.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.MimeType;

@Configuration
public class KafkaSecurityConfig {
    private final String jwkUri;

    public KafkaSecurityConfig(@Value("${jwk.uri}") String jwkUri) {
        this.jwkUri = jwkUri;
    }

    @Bean
    public MessageConverter customMessageConverter() {
        return new AbstractMessageConverter(MimeType.valueOf("application/json")) {
            @Override
            protected boolean supports(Class<?> clazz) {
                return true;
            }

            @Override
            protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
                String jwt = message.getHeaders().get("Authorization", String.class);
                SecurityContextHolder.getContext().setAuthentication(
                        new JwtAuthenticationToken(NimbusJwtDecoder.withJwkSetUri(jwkUri).build().decode(jwt)));

                return super.convertFromInternal(message, targetClass, conversionHint);
            }
        };
    }
}
